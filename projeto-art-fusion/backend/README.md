### Guia de deploy

- gradle build (coloque seu java para 21)
- apague o arquivo deploy.jar na pasta /deploy do servidor remoto
- scp -i /home/joao/cache-fast/art-key1.pem art-fusion-0.0.1-SNAPSHOT.jar ubuntu@52.67.57.216:/home/ubuntu/deploy/deploy.jar
- espere
- acesse 52.67.57.216