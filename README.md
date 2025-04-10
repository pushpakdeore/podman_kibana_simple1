podman-compose up --build

podman build -t employeemodel .
podman run -d --name myappemployee employeemodel
podman run -d --name myappemployee -p 8080:8080 employeemodel

podman exec -it id  mysql -u root -p

stop all 
docker stop $(docker ps -q)

docker rmi $(docker images -q)



podman build -t user-service:latest ./user-service 

podman build -t notification-service:latest ./notification-service

podman ps
podman ps -a
podman imges
podman start 
podman stop
podman rmi id 
podman restart id
podman logs <container>
podman rename <old_name> <new_name>

List volumes	podman volume ls
Create a volume	podman volume create <volume_name>
Remove a volume	podman volume rm <volume_name>
Inspect a volume	podman volume inspect <volume_name>

List networks	podman network ls
Inspect a network	podman network inspect <network_name>
Create a network	podman network create <network_name>
Remove a network	podman network rm <network_name>
