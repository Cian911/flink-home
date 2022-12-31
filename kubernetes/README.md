# Kubernetes Manifests

Run the following commands to apply k8s manifests in cluster.

```bash
# InfluxDb

### Apply secrets
kubectl create secret generic influxdb-secrets --from-file=kubernetes/influxdb/manifest.yaml

### Apply resources
kubectl apply -f kubernetes/influxdb/manifest.yaml

# Flink
kubectl apply -f kubernetes/flink/manifest.yaml
```
