# SmartCityX Synthetic Demonstration Datasets

This directory contains deterministic, clearly labelled synthetic datasets modeled for the **SmartCityX — Integrated Smart-City Analytics Platform** (DSA-3 Capstone).

> **Disclaimer**: All records in this directory are simulated for algorithmic demonstration, benchmarking, and academic coursework. They do not represent live or real municipal records.

---

## Dataset Catalog & Schemas

### 1. Citizen & Municipal Service Records
- **`citizen_reports.csv`** (120 records):
  - `id` (int): Unique report ID.
  - `citizen_name` (string): Name of submitting citizen.
  - `location` (string): City zone or intersection.
  - `category` (string): `POTHOLE`, `STREETLIGHT`, `WATER_LEAK`, `WASTE`, `TRAFFIC_SIGNAL`, `NOISE`, `INFRASTRUCTURE`.
  - `description` (string): Detailed problem description for string search (KMP, Z-Algorithm, Rabin-Karp, Aho-Corasick).
  - `priority` (string): `LOW`, `MEDIUM`, `HIGH`, `URGENT`.
  - `status` (string): `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`.
  - `reported_timestamp` (ISO-8601): Incident timestamp.

- **`service_requests.csv`** (80 records):
  - `id` (int): Work order ID.
  - `requester_name` (string): Submitting authority or officer.
  - `department` (string): Responsible municipal division.
  - `location` (string): Service site.
  - `request_details` (string): Scope of work and engineering instructions.
  - `priority_score` (int, 10–100): Weighted score for Randomized QuickSort prioritization.
  - `status` (string): Current progression.
  - `estimated_hours` (int): Estimated man-hours for completion.

- **`emergency_reports.csv`** (50 records):
  - `id` (int): Emergency dispatch ticket.
  - `emergency_type` (string): `FIRE_OUTBREAK`, `TRAFFIC_COLLISION`, `STRUCTURAL_COLLAPSE`, `WATER_MAIN_BURST`, `HAZMAT_SPILL`, `POWER_OUTAGE_HOSPITAL`.
  - `location` (string): Geographical site.
  - `severity` (string): `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`.
  - `description` (string): Hazard details.
  - `teams_required` (int, 1–3): Required responding squads for bipartite matching.
  - `reported_timestamp` (ISO-8601): Dispatch timestamp.
  - `active` (boolean): Whether incident is currently unresolved.

### 2. Operational & Technical Documents
- **`infrastructure_documents.jsonl`** (60 JSON lines):
  - `id` (int): Document ID.
  - `title` (string): Manual or regulatory standard title.
  - `department` (string): Issuing authority.
  - `category` (string): Regulatory category.
  - `content` (string): High-density technical text indexed via Suffix Array (SA-IS), Suffix Automaton, Kasai LCP, and sequence alignments.
  - `version` (string): Semantic standard version.
  - `created_date` (ISO-8601 Date): Publication date.

### 3. Transportation & Traffic Flow
- **`traffic_nodes.csv`** (25 nodes):
  - `node_id` (int, 0–24): Network vertex identifier.
  - `name` (string): Junction identifier.
  - `zone` (string): City sector.
  - `latitude`, `longitude` (float): Spatial coordinates.
- **`traffic_edges.csv`** (50 edges):
  - `source`, `target` (int): Connected junction IDs.
  - `capacity_vehicles_per_hr` (int, 800–3600): Edge flow capacity used in Dinic and Edmonds-Karp max-flow.
  - `free_flow_time_min` (int): Uncongested transit duration.
  - `length_km` (float): Physical distance.

### 4. Utilities & Grid Networks
- **`water_resources.csv`** (20 records):
  - `id` (int): Facility ID.
  - `facility_name` (string): Reservoir or filtration plant name.
  - `location` (string): City district.
  - `capacity_mld` (int, Megaliters/Day): Maximum storage/treatment capacity.
  - `current_level_pct` (int, 0–100): Current capacity percentage.
  - `status` (string): Operational status.
  - `daily_output_mld` (float): Daily delivered output.
- **`energy_resources.csv`** (20 records):
  - `id` (int): Substation ID.
  - `grid_station` (string): Station name.
  - `location` (string): Municipal sector.
  - `rated_mva` (int, Megavolt-amperes): Nameplate capacity.
  - `current_load_pct` (int, 0–100): Grid load factor.
  - `source_type` (string): `SOLAR_ARRAY`, `THERMAL_TURBINE`, `HYDRO_PUMPED`, `WIND_SUBSTATION`, `BIOMASS_COGEN`.
  - `unit_cost_usd_kwh` (float): Generation cost.
- **`resource_demands.csv`** (35 records):
  - `demand_id` (int): Zone demand ticket.
  - `consumer_zone` (string): Sector demanding utilities.
  - `resource_type` (string): Utility category.
  - `required_quantity` (int): Required units for Bitmask and Subset-Sum DP optimization.
  - `max_budget_usd` (int): Upper bound on allocated expenditure.
  - `criticality` (string): `NORMAL`, `HIGH`, `VITAL`.

### 5. Emergency Allocation & Incident Matching
- **`emergency_teams.csv`** (15 teams):
  - `team_id` (int, 1–15): Squad ID.
  - `team_name` (string): Station division.
  - `base_station` (string): Origin station.
  - `specialty` (string): `FIRE_HAZMAT`, `CARDIAC_AMBULANCE`, `SEARCH_RESCUE`, `FLOOD_RECOVERY`, `POWER_RESTORATION`.
  - `crew_count` (int): Active personnel.
  - `status` (string): `AVAILABLE`, `DEPLOYED`.
- **`assignment_requirements.csv`** (24 requirements):
  - `req_id` (int): Allocation task requirement.
  - `incident_id` (int): Associated emergency ticket.
  - `location` (string): Destination.
  - `required_specialty` (string): Matching capability.
  - `priority` (string): `HIGH`, `CRITICAL`.
  - `eligible_teams` (semicolon-delimited int list): Compatible team IDs for Bipartite Matching.

### 6. Municipal Project Scheduling (CPM)
- **`project_tasks.csv`** (24 tasks):
  - `task_id` (int, 1–24): Activity node.
  - `task_name` (string): Engineering phase.
  - `duration_days` (int): Duration in calendar days.
  - `department` (string): Managing bureau.
  - `estimated_budget` (int): Projected expense.
- **`project_dependencies.csv`** (30 edges):
  - `predecessor_id`, `successor_id` (int): Directed dependency edge.
  - `lag_days` (int): Mandatory interval between completion and initiation.

### 7. Citywide Infrastructure Graph (NP & Approximation)
- **`infrastructure_vertices.csv`** (22 vertices):
  - `vertex_id` (int, 1–22): Node ID.
  - `name` (string): Installation name.
  - `facility_type` (string): Utility classification.
  - `criticality_score` (int, 40–99): Weight used in Vertex-Cover 2-Approximation and Parameterized FPT.
- **`infrastructure_edges.csv`** (35 edges):
  - `u`, `v` (int): Infrastructure connection link.
  - `connection_type` (string): `FIBER_BACKBONE`, `PRIMARY_POWER_FEEDER`, `WATER_TRUNK_LINE`, `SURFACE_ARTERIAL`.
  - `distance_km` (float): Segment length.
  - `bandwidth_gbps` (int): Channel capacity.

### 8. Continuous IoT & Sensor Telemetry
- **`iot_sensor_readings.csv`** (1,200 readings):
  - `reading_id` (int, 1–1200): Observation ID.
  - `sensor_id` (string): Unique device tag (`SENSOR-IOT-100` to `149`).
  - `sensor_type` (string): `TEMPERATURE`, `HUMIDITY`, `AIR_QUALITY_AQI`, `TRAFFIC_FLOW`, `WATER_PRESSURE`, `ENERGY_GRID_LOAD`, `NOISE_LEVEL`, `STRUCTURAL_VIBRATION`.
  - `value` (float): Numerical measurement.
  - `unit` (string): Engineering unit (°C, %, AQI, veh/min, bar, MW, dB, mm/s).
  - `location` (string): Stationed district.
  - `timestamp` (ISO-8601): Observation moment.
  - `status` (string): `NORMAL`, `WARNING`, `ALERT`.
