package smartcity.dataset;

import smartcity.model.*;
import smartcity.model.enums.*;
import smartcity.repository.*;
import smartcity.util.CSVParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Enterprise loader for all SmartCityX CSV and JSONL datasets.
 * Features robust RFC-4180 parsing, multi-path fallback, and statistics tracking.
 */
public class SmartCityDatasetLoader {

    public static class LoadStats {
        public int citizenReports = 0;
        public int serviceRequests = 0;
        public int emergencyReports = 0;
        public int documents = 0;
        public int trafficNodes = 0;
        public int trafficEdges = 0;
        public int waterResources = 0;
        public int energyResources = 0;
        public int resourceDemands = 0;
        public int emergencyTeams = 0;
        public int assignmentRequirements = 0;
        public int projectTasks = 0;
        public int projectDependencies = 0;
        public int infrastructureVertices = 0;
        public int infrastructureEdges = 0;
        public int sensorReadings = 0;
        public long durationMs = 0;

        @Override
        public String toString() {
            return String.format(
                "Datasets Loaded: %,d Citizen Reports | %,d Service Reqs | %,d Emergencies | %,d Documents | %,d Sensors (in %d ms)",
                citizenReports, serviceRequests, emergencyReports, documents, sensorReadings, durationMs
            );
        }
    }

    private static File resolveDataFile(String filename) {
        // Try ./data/filename
        File f = new File("data", filename);
        if (f.exists()) return f;

        // Try ../data/filename (if running from subfolder)
        f = new File("../data", filename);
        if (f.exists()) return f;

        // Try absolute path if current working directory has data
        File root = new File(".");
        f = new File(root, "data/" + filename);
        if (f.exists()) return f;

        return null;
    }

    private static BufferedReader openReader(String filename) throws IOException {
        File file = resolveDataFile(filename);
        if (file != null && file.exists()) {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        }

        // Fallback to classpath resource
        InputStream is = SmartCityDatasetLoader.class.getResourceAsStream("/" + filename);
        if (is != null) {
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        }
        is = SmartCityDatasetLoader.class.getResourceAsStream("/data/" + filename);
        if (is != null) {
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        }

        throw new FileNotFoundException("Cannot locate dataset file: " + filename);
    }

    public static LoadStats loadAll(
            CitizenReportRepository citizenRepo,
            ServiceRequestRepository serviceRepo,
            EmergencyReportRepository emergencyRepo,
            DocumentRepository docRepo,
            IndexRepository indexRepo,
            TrafficRepository trafficRepo,
            ResourceRepository resourceRepo,
            EmergencyTeamRepository teamRepo,
            ProjectRepository projectRepo,
            InfrastructureRepository infraRepo,
            SensorRepository sensorRepo) {

        long start = System.currentTimeMillis();
        LoadStats stats = new LoadStats();

        if (citizenRepo != null) citizenRepo.clear();
        if (serviceRepo != null) serviceRepo.clear();
        if (emergencyRepo != null) emergencyRepo.clear();
        if (docRepo != null) docRepo.clear();
        if (trafficRepo != null) trafficRepo.clear();
        if (resourceRepo != null) resourceRepo.clear();
        if (teamRepo != null) teamRepo.clear();
        if (projectRepo != null) projectRepo.clear();
        if (infraRepo != null) infraRepo.clear();
        if (sensorRepo != null) sensorRepo.clear();

        // 1. Citizen Reports
        try (BufferedReader br = openReader("citizen_reports.csv")) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 8) {
                    citizenRepo.add(new CitizenReport(
                            Integer.parseInt(p[0]), p[1], p[2],
                            ReportCategory.fromString(p[3]), p[4],
                            ReportPriority.fromString(p[5]),
                            ReportStatus.fromString(p[6]), p[7]
                    ));
                    stats.citizenReports++;
                }
            }
        } catch (Exception ignored) {}

        // 2. Service Requests
        try (BufferedReader br = openReader("service_requests.csv")) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 8) {
                    serviceRepo.add(new ServiceRequest(
                            Integer.parseInt(p[0]), p[1], p[2], p[3], p[4],
                            Integer.parseInt(p[5]), ReportStatus.fromString(p[6]),
                            Integer.parseInt(p[7])
                    ));
                    stats.serviceRequests++;
                }
            }
        } catch (Exception ignored) {}

        // 3. Emergency Reports
        try (BufferedReader br = openReader("emergency_reports.csv")) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 8) {
                    emergencyRepo.add(new EmergencyReport(
                            Integer.parseInt(p[0]), p[1], p[2],
                            EmergencySeverity.fromString(p[3]), p[4],
                            Integer.parseInt(p[5]), p[6],
                            Boolean.parseBoolean(p[7])
                    ));
                    stats.emergencyReports++;
                }
            }
        } catch (Exception ignored) {}

        // 4. Infrastructure Documents (JSONL)
        try (BufferedReader br = openReader("infrastructure_documents.jsonl")) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Document doc = parseJsonDocument(line);
                if (doc != null) {
                    docRepo.add(doc);
                    stats.documents++;
                }
            }
            if (indexRepo != null && docRepo.size() > 0) {
                indexRepo.buildIndex(docRepo);
            }
        } catch (Exception ignored) {}

        // 5. Traffic Nodes & Edges
        try (BufferedReader br = openReader("traffic_nodes.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 5) {
                    trafficRepo.addNode(new TrafficNode(
                            Integer.parseInt(p[0]), p[1], p[2],
                            Double.parseDouble(p[3]), Double.parseDouble(p[4])
                    ));
                    stats.trafficNodes++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("traffic_edges.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 5) {
                    trafficRepo.addEdge(new TrafficEdge(
                            Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]), Integer.parseInt(p[3]),
                            Double.parseDouble(p[4])
                    ));
                    stats.trafficEdges++;
                }
            }
        } catch (Exception ignored) {}

        // 6. Water & Energy Resources & Demands
        try (BufferedReader br = openReader("water_resources.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 7) {
                    resourceRepo.addWater(new WaterResource(
                            Integer.parseInt(p[0]), p[1], p[2],
                            Integer.parseInt(p[3]), Integer.parseInt(p[4]),
                            p[5], Double.parseDouble(p[6])
                    ));
                    stats.waterResources++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("energy_resources.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 7) {
                    resourceRepo.addEnergy(new EnergyResource(
                            Integer.parseInt(p[0]), p[1], p[2],
                            Integer.parseInt(p[3]), Integer.parseInt(p[4]),
                            p[5], Double.parseDouble(p[6])
                    ));
                    stats.energyResources++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("resource_demands.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 6) {
                    resourceRepo.addDemand(new ResourceDemand(
                            Integer.parseInt(p[0]), p[1], p[2],
                            Integer.parseInt(p[3]), Integer.parseInt(p[4]), p[5]
                    ));
                    stats.resourceDemands++;
                }
            }
        } catch (Exception ignored) {}

        // 7. Emergency Teams & Assignment Requirements
        try (BufferedReader br = openReader("emergency_teams.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 6) {
                    teamRepo.addTeam(new EmergencyTeam(
                            Integer.parseInt(p[0]), p[1], p[2], p[3],
                            Integer.parseInt(p[4]), p[5]
                    ));
                    stats.emergencyTeams++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("assignment_requirements.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 6) {
                    String[] teamStr = p[5].split(";");
                    int[] tIds = new int[teamStr.length];
                    for (int k = 0; k < teamStr.length; k++) {
                        try { tIds[k] = Integer.parseInt(teamStr[k].trim()); } catch (Exception ignored) {}
                    }
                    teamRepo.addRequirement(new AssignmentRequirement(
                            Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            p[2], p[3], p[4], tIds
                    ));
                    stats.assignmentRequirements++;
                }
            }
        } catch (Exception ignored) {}

        // 8. Project Tasks & Precedences
        try (BufferedReader br = openReader("project_tasks.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 5) {
                    projectRepo.addTask(new ProjectTask(
                            Integer.parseInt(p[0]), p[1],
                            Integer.parseInt(p[2]), p[3],
                            Integer.parseInt(p[4])
                    ));
                    stats.projectTasks++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("project_dependencies.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 3) {
                    projectRepo.addDependency(new ProjectDependency(
                            Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            Integer.parseInt(p[2])
                    ));
                    stats.projectDependencies++;
                }
            }
        } catch (Exception ignored) {}

        // 9. Infrastructure Graph
        try (BufferedReader br = openReader("infrastructure_vertices.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 4) {
                    infraRepo.addVertex(new InfrastructureVertex(
                            Integer.parseInt(p[0]), p[1], p[2], Integer.parseInt(p[3])
                    ));
                    stats.infrastructureVertices++;
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = openReader("infrastructure_edges.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 5) {
                    infraRepo.addEdge(new InfrastructureEdge(
                            Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4])
                    ));
                    stats.infrastructureEdges++;
                }
            }
        } catch (Exception ignored) {}

        // 10. IoT Sensor Readings
        try (BufferedReader br = openReader("iot_sensor_readings.csv")) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = CSVParser.parseLine(line);
                if (p.length >= 8) {
                    sensorRepo.add(new SensorReading(
                            Integer.parseInt(p[0]), p[1], p[2],
                            Double.parseDouble(p[3]), p[4], p[5], p[6], p[7]
                    ));
                    stats.sensorReadings++;
                }
            }
        } catch (Exception ignored) {}

        stats.durationMs = System.currentTimeMillis() - start;
        return stats;
    }

    private static Document parseJsonDocument(String json) {
        try {
            int id = extractInt(json, "id");
            String title = extractString(json, "title");
            String content = extractString(json, "content");
            if (content.isEmpty()) content = extractString(json, "text");
            String dept = extractString(json, "department");
            String date = extractString(json, "created_date");
            return new Document(id, title, content, dept.isEmpty() ? "Municipal Archive" : dept, date);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return "";
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon == -1) return "";
        int startQuote = json.indexOf('\"', colon + 1);
        if (startQuote == -1) return "";

        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = startQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                sb.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '\"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int extractInt(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return 0;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon == -1) return 0;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
