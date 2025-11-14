from pathlib import Path
from requests import Session
import urllib3
import os
from rdflib import Graph
from generate import ResourceGenerator, set_generators
from spec import PREFIXES
import sys

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

RQ1_USER = os.environ["RQ1_USER"]
RQ1_PASSWORD = os.environ["RQ1_PASSWORD"]
RQ1_TOOLNAME = os.environ["RQ1_TOOLNAME"]
RQ1_TOOLVERSION = os.environ["RQ1_TOOLVERSION"]

SERVICES_URL = (
    "https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE"
)


def get_shape_id(shape_uri: str) -> str:
    pos = shape_uri.rfind("/")
    return shape_uri[pos + 1 :]


def main():
    session = Session()
    session.verify = False
    session.auth = (RQ1_USER, RQ1_PASSWORD)
    session.headers.update(
        {
            "OSLC-Core-Version": "2.0",
            "x-requester": f"toolname={RQ1_TOOLNAME};toolversion={RQ1_TOOLVERSION};user={RQ1_USER}",
        }
    )

    res = session.get(SERVICES_URL, headers={"Accept": "application/rdf+xml"})
    res.raise_for_status()

    g = Graph()
    g.parse(data=res.content, format="xml")
    for k, v in PREFIXES.items():
        g.bind(k, v)

    rows = g.query("""
                SELECT ?label ?shape ?searchTerms WHERE {
                    ?s rdf:type oslc:QueryCapability .
                    ?s oslc:label ?label .
                    ?s cq:supportsOslcSearchTerms ?searchTerms .
                    ?s oslc:resourceShape ?shape .
                } 
            """)
    resources = [
        {
            "name": str(row.label),
            "support_search_terms": True
            if row.searchTerms.toPython() == "true"
            else False,
            "shape_id": get_shape_id(str(row.shape)),
        }
        for row in rows
    ]

    dir = sys.argv[1]

    generator_map = {}
    for resource in resources:
        generator = ResourceGenerator(
            session,
            resource["shape_id"],
            resource["support_search_terms"],
            resource["name"],
        )
        generator_map[resource["shape_id"]] = generator
        print(resource["name"], "parsed")
    set_generators(generator_map)
    for resource in resources:
        generator = generator_map[resource["shape_id"]]
        generator.generate_resource_file(Path(dir))
        print(generator.class_name(), "generated")
    with open(Path(dir).joinpath("models.py"), "w") as f:
        content = ""
        class_names = []
        for generator  in generator_map.values():
            class_name: str = generator.class_name()
            file_name: str = generator.file_name()
            content += f"from .{file_name} import {class_name}, {class_name}Property, create_{class_name.lower()}_changeset\n"
            class_names.append(generator.class_name())
        content += "\n"
        for name in class_names:
            content += f"{name}.model_rebuild()\n"
        f.write(content)


if __name__ == "__main__":
    main()
