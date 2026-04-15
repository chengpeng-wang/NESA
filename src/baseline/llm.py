from concurrent.futures import ThreadPoolExecutor, TimeoutError
import argparse
from openai import *
import tiktoken
from typing import Any, Callable, Optional, Tuple
from utils import *
import time
import json
import concurrent.futures
import boto3
from botocore.config import Config


class LLM:
    """
    An online inference model using ChatGPT
    """

    def __init__(
        self,
        online_model_name: str,
        openai_key: str,
        temperature: float,
        measure_token_cost: bool,
    ) -> None:
        self.online_model_name = online_model_name
        self.openai_key = openai_key
        self.temperature = temperature
        self.measure_token_cost = measure_token_cost
        self.encoding = self.initialize_encoding()

        self.systemRole = "You are an experienced static analysis assistant and are good at understanding source code."
        return

    def initialize_encoding(self) -> Optional[Any]:
        if not self.measure_token_cost:
            return None

        for loader in (
            lambda: tiktoken.encoding_for_model(self.online_model_name),
            lambda: tiktoken.get_encoding("cl100k_base"),
        ):
            try:
                return loader()
            except Exception:
                continue
        return None

    def count_tokens(self, text: str) -> int:
        if not self.measure_token_cost:
            return 0
        if self.encoding is None:
            return 0 if not text else max(1, len(text) // 4)
        try:
            return len(self.encoding.encode(text))
        except Exception:
            return 0 if not text else max(1, len(text) // 4)

    def infer(self, message: str) -> Tuple[str, int, int]:
        is_measure_cost = self.measure_token_cost
        print(self.online_model_name, "is running")
        print(message)

        output = ""
        if "gpt" in self.online_model_name or "o1-preview" in self.online_model_name:
            output = self.infer_with_openai_model(message)
        elif "claude" in self.online_model_name:
            output = self.infer_with_claude(message)
        elif "o4-mini" in self.online_model_name or "o3-mini" in self.online_model_name:
            output = self.infer_with_on_mini_model(message)
        else:
            raise ValueError(f"Unsupported inference model: {self.online_model_name}")
        input_token_cost = (
            0
            if not is_measure_cost
            else self.count_tokens(self.systemRole) + self.count_tokens(message)
        )
        output_token_cost = 0 if not is_measure_cost else self.count_tokens(output)

        print("-----output------")
        print(output)
        return output, input_token_cost, output_token_cost

    def run_with_timeout(self, func: Callable[[], str], timeout: float) -> str:
        """Run a function with timeout that works in multiple threads"""
        with concurrent.futures.ThreadPoolExecutor(max_workers=1) as executor:
            future = executor.submit(func)
            try:
                return future.result(timeout=timeout)
            except concurrent.futures.TimeoutError:
                ("Operation timed out")
                return ""
            except Exception as e:
                print(f"Operation failed: {e}")
                return ""

    def infer_claude(self, message: str) -> str:
        input = [
            {"role": "system", "content": self.systemRole},
            {"role": "user", "content": message},
        ]

        def call_claude() -> str:
            openai.api_key = self.openai_key
            response = openai.ChatCompletion.create(
                model=self.online_model_name,
                messages=input,
                temperature=self.temperature,
            )
            return response.choices[0].message.content

        return self.call_with_timeout(call_claude, 60)

    def infer_with_openai_model(self, message: str) -> str:
        model_input = [
            {"role": "system", "content": self.systemRole},
            {"role": "user", "content": message},
        ]

        def call_openai() -> str:
            client = OpenAI(api_key=self.openai_key)
            response = client.chat.completions.create(
                model=self.online_model_name,
                messages=model_input,
                temperature=self.temperature,
            )
            return response.choices[0].message.content

            # openai.api_key = self.openai_key
            # response = openai.ChatCompletion.create(
            #     model=self.online_model_name,
            #     messages=model_input,
            #     temperature=self.temperature,
            # )
            # return response.choices[0].message.content

        return self.call_with_timeout(call_openai, 10000)

    def call_with_timeout(self, func: Callable[[], str], timeout: float) -> str:
        with ThreadPoolExecutor() as executor:
            future = executor.submit(func)
            try:
                result = future.result(timeout=timeout)
                return result
            except TimeoutError:
                print("Call timed out")
                future.cancel()
                return ""
            except Exception as e:
                print("Error during call:", e)
                return ""

    def infer_with_on_mini_model(self, message: str) -> str:
        model_input = [
            {"role": "system", "content": self.systemRole},
            {"role": "user", "content": message},
        ]

        def call_api() -> str:
            client = OpenAI(api_key=self.openai_key)
            response = client.chat.completions.create(
                model=self.online_model_name, messages=model_input
            )
            return response.choices[0].message.content

        tryCnt = 0
        while tryCnt < 5:
            tryCnt += 1
            try:
                output = self.run_with_timeout(call_api, timeout=100)
                if output:
                    return output
            except Exception as e:
                print(f"API error: {e}")
            time.sleep(2)

        return ""

    def infer_with_claude(self, message: str) -> str:
        """Infer using the Claude model via AWS Bedrock"""
        model_input = [
            {
                "role": "assistant",
                "content": self.systemRole,
            },
            {"role": "user", "content": message},
        ]

        body = json.dumps(
            {
                "messages": model_input,
                "max_tokens": 4000,
                "anthropic_version": "bedrock-2023-05-31",
                "temperature": self.temperature,
                "top_k": 50,
            }
        )

        def call_api() -> str:
            client = boto3.client(
                "bedrock-runtime",
                region_name="us-west-2",
                config=Config(read_timeout=100),
            )

            response = (
                client.invoke_model(
                    modelId=self.online_model_name,
                    contentType="application/json",
                    body=body,
                )["body"]
                .read()
                .decode("utf-8")
            )

            response = json.loads(response)
            return response["content"][0]["text"]

        tryCnt = 0
        while tryCnt < 5:
            tryCnt += 1
            try:
                output = self.run_with_timeout(call_api, timeout=100)
                if output:
                    return output
            except Exception as e:
                print(e)
            time.sleep(2)

        return ""


# Usage example
if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--inference-model",
        type=str,
        default="gpt-4o-mini",
        help="Specify the LLM model for inference. Defaults to gpt-4o-mini.",
    )
    parser.add_argument(
        "--temperature",
        type=float,
        default=0.0,
        help="Specify the sampling temperature for the LLM. Defaults to 0.0.",
    )
    parser.add_argument(
        "--message",
        type=str,
        default="Sample message",
        help="Specify the message to send to the LLM",
    )
    parser.add_argument(
        "--measure-token-cost",
        action="store_true",
        help="Measure token cost for the sample invocation",
    )

    args = parser.parse_args()
    llm = LLM(
        args.inference_model,
        standard_keys[0],
        args.temperature,
        args.measure_token_cost,
    )
    output, input_token_cost, output_token_cost = llm.infer(args.message)
    print(output)
