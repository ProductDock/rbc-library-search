#!/bin/bash
sudo usermod -a -G docker ${USER}
docker-credential-gcr configure-docker

docker stop rbc-library-search
docker rm rbc-library-search
docker rmi $(docker images | grep "rbc-library-search")

docker run -dp 8081:8080 --env-file /home/pd-library/.search-service_env --name=rbc-library-search gcr.io/prod-pd-library/rbc-library-search:$1
docker container ls -a