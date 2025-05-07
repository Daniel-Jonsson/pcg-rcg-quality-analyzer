import requests
import csv
import os
import re
import sys
from dotenv import load_dotenv

load_dotenv()

# Set your SonarQube server and project key
SONAR_URL = "http://localhost:9000"
PROJECT_KEY = "attack-generation"
METRICS = "complexity,cognitive_complexity"

# Get the component tree (all files/folders)
url = f"{SONAR_URL}/api/measures/component_tree"
page_size = 500
page = 1
all_components = []

TOKEN = os.getenv("API_KEY")
if not TOKEN:
    print("API_KEY is not set in the .env file")
    sys.exit(1)

print(f"Using API key: {TOKEN}")
headers = {"Authorization": f"Bearer {TOKEN}"}

while True:
    params = {
        "component": PROJECT_KEY,
        "metricKeys": METRICS,
        "ps": page_size,
        "p": page
    }
    response = requests.get(url, params=params, headers=headers)
    if response.status_code != 200:
        print("Response text:", response.text)
        sys.exit(1)
    data = response.json()
    components = data.get('components', [])
    if not components:
        break
    all_components.extend(components)
    if len(components) < page_size:
        break
    page += 1

print(f"Total components fetched: {len(all_components)}")

# Extract relevant info
rcg_rows = []
pcg_rows = []

for component in all_components:
    path = component.get('path', '')
    # Only process compound directories, not files
    match = re.match(r'(rcg|pcg)/gen(\d+)/compound_(\d+)$', path)
    if match:
        attack_type, generation, compound_id = match.groups()
        metrics = {m['metric']: m['value'] for m in component.get('measures', [])}
        row = [generation, compound_id, metrics.get('complexity', ''), metrics.get('cognitive_complexity', '')]
        if attack_type == 'rcg':
            rcg_rows.append(row)
        elif attack_type == 'pcg':
            pcg_rows.append(row)

# Sort by generation, then compound_id (both as integers)
rcg_rows.sort(key=lambda x: (int(x[0]), int(x[1])))
pcg_rows.sort(key=lambda x: (int(x[0]), int(x[1])))

# Set output directory and file
output_dir = r"C:\Users\danne\Documents\Thesis\Analysis-data"
os.makedirs(output_dir, exist_ok=True)

with open(os.path.join(output_dir, 'rcg.csv'), 'w', newline='') as f:
    writer = csv.writer(f, delimiter=";")
    writer.writerow(['generation', 'compound_id', 'cyclomatic complexity', 'cognitive complexity'])
    # Convert all values to strings for writing
    writer.writerows([[str(i) for i in row] for row in rcg_rows])

with open(os.path.join(output_dir, 'pcg.csv'), 'w', newline='') as f:
    writer = csv.writer(f, delimiter=";")
    writer.writerow(['generation', 'compound_id', 'cyclomatic complexity', 'cognitive complexity'])
    writer.writerows([[str(i) for i in row] for row in pcg_rows])

print("Exported rcg.csv and pcg.csv to", output_dir)