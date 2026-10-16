# SmartCityX Municipal Dataset Guide & Schema Reference

SmartCityX operates on deterministic, offline municipal datasets stored under the `data/` directory. All datasets comply with RFC-4180 CSV and JSON Lines (JSONL) formatting.

---

## 1. Dataset Overview & Inventory

| Dataset File | Format | Record Count | Minimum Required | Primary Domain / Usage |
| :--- | :---: | :---: | :---: | :--- |
| `citizen_reports.csv` | CSV | 120 | $\ge 100$ | Citizen grievances, issue classifications, urgency triage |
| `service_requests.csv` | CSV | 80 | $\ge 60$ | Internal work orders, department assignment, priority queues |
| `emergency_reports.csv` | CSV | 50 | $\ge 40$ | High-urgency incidents (fires, chemical leaks, structural hazards) |
| `infrastructure_documents.jsonl`| JSONL | 60 | $\ge 50$ | Municipal building specs, transit plans, civil blueprints |
| `traffic_nodes.csv` | CSV | 25 | $\ge 20$ | Traffic intersections, highway junctions, transit hubs |
| `traffic_edges.csv` | CSV | 50 | $\ge 40$ | Roadway links, lane capacities, speeds, flow bounds |
| `water_resources.csv` | CSV | 20 | $\ge 15$ | Reservoirs, filtration plants, pumping stations, capacities |
| `energy_resources.csv` | CSV | 20 | $\ge 15$ | Substation transformers, solar microgrids, MW outputs |
| `resource_demands.csv` | CSV | 35 | $\ge 30$ | District resource quotas, consumption requirements |
| `emergency_teams.csv` | CSV | 15 | $\ge 12$ | Specialized units (HazMat, EMT, Search & Rescue) |
| `assignment_requirements.csv` | CSV | 24 | $\ge 20$ | Emergency dispatch constraints and squad qualifications |
| `project_tasks.csv` | CSV | 24 | $\ge 20$ | Capital work breakdown, durations, cost estimates |
| `project_dependencies.csv` | CSV | 30 | $\ge 25$ | Directed precedence edges for Critical Path Method (CPM) |
| `infrastructure_vertices.csv` | CSV | 22 | $\ge 20$ | Civil asset vertices for structural network optimization |
| `infrastructure_edges.csv` | CSV | 35 | $\ge 30$ | Structural connection links, pipelines, and transmission lines |
| `iot_sensor_readings.csv` | CSV | 1,200 | $\ge 1,000$ | High-frequency telemetry (PM2.5, noise, temp, flow, vibration) |

---

## 2. Detailed Schemas

### `citizen_reports.csv`
- `report_id` (Integer): Unique primary key.
- `citizen_name` (String): Fictitious reporter name.
- `category` (String): Grievance classification (`Pothole`, `Streetlight`, `Water Leak`, `Waste`, `Traffic Hazard`).
- `description` (String): Descriptive text searched via KMP, Rabin-Karp, and Levenshtein distance.
- `location` (String): District or neighborhood name.
- `priority` (Integer): Urgency level ($1 = \text{Low}, 2 = \text{Medium}, 3 = \text{High}, 4 = \text{Critical}$).
- `timestamp` (String): ISO-8601 creation timestamp.

### `traffic_nodes.csv` & `traffic_edges.csv`
- **Nodes**: `node_id` (Integer), `name` (String), `latitude` (Double), `longitude` (Double), `node_type` (`Intersection`, `Bridge`, `Highway Exit`).
- **Edges**: `edge_id` (Integer), `source_id` (Integer), `target_id` (Integer), `capacity` (Integer: vehicles/hour), `free_flow_speed` (Integer: km/h), `length_km` (Double).
- **Algorithmic Use**: Dinic's algorithm for max throughput, Min-Cut bottleneck isolation, Bitmask TSP routing.

### `emergency_teams.csv` & `assignment_requirements.csv`
- **Teams**: `team_id` (Integer), `team_name` (String), `specialty` (`Fire`, `HazMat`, `EMT`, `Rescue`), `capacity` (Integer), `base_location` (String).
- **Requirements**: `requirement_id` (Integer), `incident_id` (Integer), `required_specialty` (String), `urgency_level` (Integer).
- **Algorithmic Use**: Hopcroft-Karp / Dinic bipartite matching for incident response dispatch.

### `iot_sensor_readings.csv`
- `reading_id` (Integer): Sequential telemetry sequence identifier.
- `sensor_id` (String): Hardware asset tag (`SEN-ENV-01`, `SEN-VIB-04`, etc.).
- `metric_type` (String): `PM2.5`, `NOISE_DB`, `TEMPERATURE_C`, `VIBRATION_HZ`, `WATER_FLOW_LPS`.
- `value` (Double): Floating-point observation.
- `status` (String): `NORMAL`, `WARNING`, `CRITICAL`.
- `timestamp` (String): ISO-8601 recording timestamp.
- **Algorithmic Use**: Blelloch work-efficient parallel prefix scan, parallel reduction, universal hash sampling.

---

## 3. Ingestion & RFC-4180 Parsing

All CSV parsing is handled by `SmartCityDatasetLoader.parseCsvLine(String line)`:
- Respects double quotes enclosing fields containing commas.
- Unescapes double-quote pairs (`""` $\to$ `"`).
- Trims trailing carriage returns and whitespace.
- Falls back to bundled classpath resources if executed outside the project root directory.
