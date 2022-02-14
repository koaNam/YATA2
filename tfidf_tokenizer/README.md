To build the grpc files execute:

python -m grpc_tools.protoc -I . --python_out=. --grpc_python_out=. protos/tfidf_tokenizer.proto