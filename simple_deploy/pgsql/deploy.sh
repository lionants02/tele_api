docker run -d \
	--name tele_api \
	-e POSTGRES_PASSWORD=mysecretpassword \
	-e PGDATA=/var/lib/postgresql/data/pgdata \
	-v E:\\temp\\pg_tele_api:/var/lib/postgresql/data \
	-p 5432:5432 \
	postgres:12.13-alpine3.17
