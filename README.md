docker compose up -d

docker compose logs --tail 30 db 

docker compose exec db psql -U taskflow_user -d taskflow_db