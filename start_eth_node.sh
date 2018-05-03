#!/bin/bash
docker run -it -p 8545:8545 -p 30303:30303 -v $(pwd)/eth:/root/.ethereum ethereum/client-go:alpine --rpc --rpcaddr "0.0.0.0" --rpcvhosts=*
