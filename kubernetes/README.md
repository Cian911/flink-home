# Kubernetes Manifests

### Requirements

To perform the next set of steps, you will need to have `kubectl` installed and running. If you don't, you can find a guide [here](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/) on how to get it setup on your machine.

### Create Manifests

Run the following commands to apply k8s manifests in cluster.

##### InfluxDB

First, make any needed modifications to the influxdb manifest, such as changing secrets to your own desired configuration, changing the namespace and anything else you deem relevant. Then apply the changes.

```bash
### Apply secrets
kubectl create secret generic influxdb-secrets --from-file=kubernetes/influxdb/manifest.yaml

### Apply resources
kubectl apply -f kubernetes/influxdb/manifest.yaml
```

##### MQTT

Note, I have not tested this manifest myself, as I am using a cloud broker provided by [HiveMQ](https://console.hivemq.cloud/). You can also go ahead and use this, or continue and apply the manifests as described below, just be consiouc that you may (likely) will need to make further modifications.

```bash
### Apply resources
kubectl apply -f kubernetes/influxdb/manifest.yaml
```

##### Flink

**N.B**: The docker image associated with this manifest is a 64bit image, which means you will need to have a 64bit OS to run it. This is important if you plan to run this on a raspberrypi.

Similarly to InfluxDB, it is best to first go over the flink manifest and make any modifications you deem necessary before continuing. For instance, I have added a custom node affinity to the manifest to ensure the pods will be scheduled only on 64bit nodes in my cluster. This may not be needed if you can ensure anywhere you plan to run this it will be on a 64bit OS.

```bash
# Flink
kubectl apply -f kubernetes/flink/manifest.yaml
```
