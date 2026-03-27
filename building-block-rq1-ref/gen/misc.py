TYPE_MAP = {
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral": "str",
    "http://www.w3.org/2001/XMLSchema#boolean": "bool",
    "http://www.w3.org/2001/XMLSchema#dateTime": "datetime",
    "http://www.w3.org/2001/XMLSchema#decimal": "float",
    "http://www.w3.org/2001/XMLSchema#double": "float",
    "http://www.w3.org/2001/XMLSchema#float": "float",
    "http://www.w3.org/2001/XMLSchema#integer": "int",
    "http://www.w3.org/2001/XMLSchema#string": "str",
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString": "str",
    "http://open-services.net/ns/core#Resource": "Resource",
    "http://open-services.net/ns/core#AnyResource": "Resource",
    "http://open-services.net/ns/core#LocalResource": "Resource",
}


def sanitize_cammel_case(name: str):
    assert len(name) > 0
    name = name.replace("-", " ").title().replace(" ", "")
    if name[0].isdigit():
        name = "X" + name
    return name


def sanitize_snake_case(name: str):
    assert len(name) > 0
    name = (
        name.replace("-", "_")
        .replace(":", "__")
        .replace(" ", "_")
        .replace(".", "_")
        .lower()
    )
    if name[0].isdigit():
        name = "x" + name
    return name
