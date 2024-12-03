#!/bin/bash

docker volume create evolution-volume
docker volume create source-volume
docker volume create dataset-products-volume

docker run -v source-volume:/EvolutionSPLFramework/splsToMerge -v evolution-volume:/evolution --name helper busybox true
docker cp ./splsToMerge helper:/EvolutionSPLFramework/splsToMerge
docker cp ./resources helper:/EvolutionSPLFramework/resources
docker mkdir helper:/evolution
docker rm helper