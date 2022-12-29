mkdir -p /data/tele_api/activity_log/timescale
chmod -R 777 /data/tele_api/activity_log/timescale

echo "Deploy stack"
docker stack deploy -c ./timescale.yml tele_activity_log_db
