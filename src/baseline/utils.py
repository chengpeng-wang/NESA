import os

# Standard API
standard_keys = [os.environ.get("OPENAI_API_KEY")]

os.environ["REPLICATE_API_TOKEN"] = os.environ.get("REPLICATE_API_TOKEN")

iterative_count_bound = 3

# For dev options
DEBUG = True
scope_count_bound = 10
