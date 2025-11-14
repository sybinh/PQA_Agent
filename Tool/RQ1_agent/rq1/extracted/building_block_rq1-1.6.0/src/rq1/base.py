from typing import Generic, TypeVar, Optional, List, Dict
from pydantic import BaseModel, Field, ConfigDict


class Resource(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    uri: str = Field(title="Resource Identifier", alias="rdf:resource")

class BaseRecord(BaseModel):
    uri: Optional[str] = Field(alias="rdf:about", default=None)

def reference(uri: str) -> Resource:
    return Resource(**{"rdf:resource": uri})


class QueryStatement(object):
    def __init__(self, value: str):
        self.value = value

    def __str__(self):
        return self.value

    def __repr__(self):
        return self.value

    def __and__(self, other):
        return QueryStatement(f"{self.value} and {other.value}")

    def __or__(self, _):
        raise NotImplementedError("Operator '|' is not allowed in query")

R = TypeVar("R", bound=BaseRecord)

class QueryResult(BaseModel, Generic[R]):
    total_count: int
    next_page: Optional[str]
    members: List[R]
    member_type: type[R] = Field(repr=False, exclude=True)


def parse_query_result(rtype: type[R], data_json: Dict) -> QueryResult[R]:
    return QueryResult(
        total_count = data_json["oslc:responseInfo"]["oslc:totalCount"],
        next_page = data_json["oslc:responseInfo"].get("oslc:nextPage"),
        members = [rtype(**x) for x in data_json["rdfs:member"]],
        member_type=rtype
    )
