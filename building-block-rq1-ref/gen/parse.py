import logging
from typing import Any, Dict, Optional, List

from rdflib import Graph
from rdflib.term import URIRef
from requests import Session
from requests.auth import HTTPBasicAuth

from spec import (
    Occurrence,
    Property,
    Reference,
    Representation,
    ResourceShape,
)

logger = logging.getLogger(__name__)


def parse_description(g: Graph, id: str) -> Optional[str]:
    result = g.query(
        "SELECT ?v WHERE { <%s> <http://purl.org/dc/terms/description> ?v . }" % (id)
    )
    for row in result:
        return str(row.v)


def parse_title(g: Graph, id: str) -> Optional[str]:
    result = g.query(
        "SELECT ?v WHERE { <%s> <http://purl.org/dc/terms/title> ?v . }" % (id)
    )
    for row in result:
        return str(row.v)


def parse_property_allowedValues(g: Graph, property_def: str) -> List[Any]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#allowedValue> ?v .
            }
        """
        % (property_def)
    )
    values = [
        Reference(ref=str(row.v)) if isinstance(row.v, URIRef) else row.v.toPython()
        for row in result
    ]
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#allowedValues> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        allowed_values_id = str(row.v)
        new_result = g.query(
            "SELECT ?v WHERE { <%s> <http://open-services.net/ns/core#allowedValue> ?v .}"
            % (allowed_values_id)
        )
        for new_row in new_result:
            values.append(
                Reference(ref=str(new_row.v))
                if isinstance(new_row.v, URIRef)
                else new_row.v.toPython()
            )
    return values


def parse_property_defaultValue(g: Graph, property_def: str) -> Optional[Any]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#defaultValue> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return row.v.toPython()


def parse_hidden(g: Graph, id: str) -> Optional[bool]:
    result = g.query(
        "SELECT ?v WHERE { <%s> <http://open-services.net/ns/core#hidden> ?v .}" % (id)
    )
    for row in result:
        return row.v.toPython()


def parse_property_isMemberProperty(g: Graph, property_def: str) -> Optional[bool]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#isMemberProperty> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return row.v.toPython()


def parse_property_maxSize(g: Graph, property_def: str) -> Optional[int]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#maxSize> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return int(row.v)


def parse_property_name(g: Graph, property_def: str) -> str:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#name> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return str(row.v)
    raise Exception("olsc:name not found")


def parse_property_occurs(g: Graph, property_def: str) -> Occurrence:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#occurs> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        occurs = str(row.v)
        if occurs == Occurrence.ZERO_OR_ONE:
            return Occurrence.ZERO_OR_ONE
        if occurs == Occurrence.EXACTLY_ONE:
            return Occurrence.EXACTLY_ONE
        if occurs == Occurrence.ZERO_OR_MANY:
            return Occurrence.ZERO_OR_MANY
        if occurs == Occurrence.ONE_OR_MANY:
            return Occurrence.ONE_OR_MANY
        raise ValueError(f"Unknown occurence: `{occurs}`")
    raise Exception("oslc:occurs not found")


def parse_property_queryable(g: Graph, property_def: str) -> Optional[bool]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#queryable> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return row.v.toPython()


def parse_property_range(g: Graph, property_def: str) -> List[Reference]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#range> ?v .
            }
        """
        % (property_def)
    )
    return [Reference(ref=str(row.v)) for row in result]


def parse_property_readOnly(g: Graph, property_def: str) -> Optional[bool]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#readOnly> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return row.v.toPython()


def parse_property_representation(
    g: Graph, property_def: str
) -> Optional[Representation]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#representation> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        representation = str(row.v)
        if representation == Representation.EITHER:
            return Representation.EITHER
        if representation == Representation.INLINE:
            return Representation.INLINE
        if representation == Representation.REFERENCE:
            return Representation.REFERENCE
        raise ValueError(f"Unknown representation: `{representation}`")


def parse_property_valueShape(g: Graph, property_def: str) -> Optional[Reference]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#valueShape> ?v .
            }
        """
        % (property_def)
    )
    for row in result:
        return Reference(ref=str(row.v))


def parse_property_valueType(g: Graph, property_def: str) -> List[Reference]:
    result = g.query(
        """
            SELECT ?v
            WHERE
            {
                ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                ?node <http://open-services.net/ns/core#valueType> ?v .
            }
        """
        % (property_def)
    )
    return [Reference(ref=str(row.v)) for row in result]


def parse_describes(g: Graph, id: str) -> List[Reference]:
    result = g.query(
        "SELECT ?v WHERE { <%s> <http://open-services.net/ns/core#describes> ?v .}"
        % (id)
    )
    return [Reference(ref=str(row.v)) for row in result]


def parse_property_description(g: Graph, property_def: str) -> Optional[str]:
    result = g.query(
        """
                         SELECT ?v
                         WHERE
                         {
                             ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                             ?node <http://purl.org/dc/terms/description> ?v .
                         }
        """
        % (property_def)
    )
    for row in result:
        return str(row.v)


def parse_property_title(g: Graph, property_def: str) -> Optional[str]:
    result = g.query(
        """
                         SELECT ?v
                         WHERE
                         {
                             ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                             ?node <http://purl.org/dc/terms/title> ?v .
                         }
        """
        % (property_def)
    )
    for row in result:
        return str(row.v)


def parse_property_hidden(g: Graph, property_def: str) -> Optional[bool]:
    result = g.query(
        """
                         SELECT ?v
                         WHERE
                         {
                             ?node <http://open-services.net/ns/core#propertyDefinition> <%s> .
                             ?node <http://open-services.net/ns/core#hidden> ?v .
                         }
        """
        % (property_def)
    )
    for row in result:
        return row.v.toPython()


def parse_properties(g: Graph, property_def: str) -> List[Property]:
    result = g.query(
        """
          SELECT ?v
          WHERE
          {
              <%s> <http://open-services.net/ns/core#property> ?node .
              ?node <http://open-services.net/ns/core#propertyDefinition> ?v .
          }
      """
        % (property_def)
    )
    properties = []
    for row in result:
        property_def = str(row.v)
        properties.append(
            Property(
                description=parse_property_description(g, property_def),
                title=parse_property_title(g, property_def),
                allowedValues=parse_property_allowedValues(g, property_def),
                defaultValue=parse_property_defaultValue(g, property_def),
                hidden=parse_property_hidden(g, property_def),
                isMemberProperty=parse_property_isMemberProperty(g, property_def),
                maxSize=parse_property_maxSize(g, property_def),
                name=parse_property_name(g, property_def),
                occurs=parse_property_occurs(g, property_def),
                propertyDefinition=Reference(ref=property_def),
                queryable=parse_property_queryable(g, property_def),
                range=parse_property_range(g, property_def),
                readOnly=parse_property_readOnly(g, property_def),
                representation=parse_property_representation(g, property_def),
                valueShape=parse_property_valueShape(g, property_def),
                valueType=parse_property_valueType(g, property_def),
            )
        )
    return properties


def parse_resourceShape(g: Graph) -> ResourceShape:
    result = g.query(
        "SELECT ?id WHERE { ?id <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://open-services.net/ns/core#ResourceShape> .}"
    )
    for row in result:
        id = str(row.id)
        return ResourceShape(
            description=parse_description(g, id),
            title=parse_title(g, id),
            describes=parse_describes(g, id),
            hidden=parse_hidden(g, id),
            properties=parse_properties(g, id),
        )
    raise Exception("ResourceShape not found")


def parse_prefixes(g: Graph) -> dict[str, str]:
    result = g.query("""
                         SELECT ?prefix ?url
                         WHERE {
                             ?s <http://open-services.net/ns/core#prefixDefinition> ?o .
                             ?o <http://open-services.net/ns/core#prefix> ?prefix .
                             ?o <http://open-services.net/ns/core#prefixBase> ?url .
                         }
                     """)
    prefixes = {}
    for row in result:
        prefixes[str(row.prefix)] = str(row.url)
    return prefixes


class ResourceShapeParser(object):
    def __init__(
        self,
        url: str,
        username: Optional[str] = None,
        password: Optional[str] = None,
        headers: Optional[Dict[str, str]] = None,
        session: Optional[Session] = None,
        rdf_format: str = "xml",
        namespaces: dict[str, str] = {},
    ):
        if session is None:
            self.session = Session()
            self.session.verify = False
            if username is not None and password is not None:
                self.session.auth = HTTPBasicAuth(username, password)
            if headers is not None:
                self.session.headers.update(headers)
        else:
            self.session = session
        res = self.session.get(url, headers={"Accept": "application/rdf+xml"})
        res.raise_for_status()
        self.graph = Graph()
        self.graph.parse(data=res.text, format=rdf_format)
        self.add_namespaces(namespaces)

    def add_namespaces(self, namespaces: dict[str, str]):
        for k, v in namespaces.items():
            self.graph.bind(k, v)

    def get_namespaces(self) -> dict[str, str]:
        return dict([(x[0], str(x[1])) for x in self.graph.namespaces()])

    def parse(self) -> ResourceShape:
        return parse_resourceShape(self.graph)
