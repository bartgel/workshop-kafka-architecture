# Simple kafka


.  Delete all containers using the following command:
    ```bash
    docker rm -f $(docker ps -a -q)
    ```
    
3.  Delete all volumes using the following command:
    ```bash
    docker volume rm $(docker volume ls -q)
    ```
docker container prune
