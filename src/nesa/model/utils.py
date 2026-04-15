import os

# Standard API
standard_keys = [os.environ.get("OPENAI_API_KEY")]

replicate_api_token = os.environ.get("REPLICATE_API_TOKEN")
if replicate_api_token is not None:
    os.environ["REPLICATE_API_TOKEN"] = replicate_api_token

iterative_count_bound = 3

# For dev options
DEBUG = True
scope_count_bound = 10
