export ROOT_DATA_PATH=/data/tele-api/grafana
export SERVICE_NAME=tele_api_grafana

echo "ROOT_DATA_PATH=${ROOT_DATA_PATH}"
echo "SERVICE_NAME=${SERVICE_NAME}"

echo "Create folder ${ROOT_DATA_PATH}/datasources"
mkdir -p ${ROOT_DATA_PATH}/datasources
echo "Create folder ${ROOT_DATA_PATH}/lib_grafana"
mkdir -p ${ROOT_DATA_PATH}/lib_grafana

mkdir -p ${ROOT_DATA_PATH}/dashboard

touch ${ROOT_DATA_PATH}/dashboard/dashboard.json

cp grafana.ini ${ROOT_DATA_PATH}/grafana.ini

echo "Change mod foler ${ROOT_DATA_PATH}/grafana"
chmod -R 777 ${ROOT_DATA_PATH}

echo "Deploy stack"
docker stack deploy -c ./grafana-stack.yml ${SERVICE_NAME}
