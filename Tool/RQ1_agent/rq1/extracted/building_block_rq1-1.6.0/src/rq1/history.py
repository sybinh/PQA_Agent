
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas



class History(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    action_name: typing.Optional[typing.Literal["Copy_Record", "Import", "Submit"]] = Field(alias="cq:action_name", default=None)
    
    action_timestamp: typing.Optional[datetime] = Field(alias="cq:action_timestamp", default=None)
    
    comments: typing.Optional[str] = Field(alias="cq:comments", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    entity_dbid: typing.Optional[int] = Field(alias="cq:entity_dbid", default=None)
    
    entitydef_id: typing.Optional[int] = Field(alias="cq:entitydef_id", default=None)
    
    entitydef_name: typing.Optional[str] = Field(alias="cq:entitydef_name", default=None)
    
    expired_timestamp: typing.Optional[datetime] = Field(alias="cq:expired_timestamp", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[int] = Field(alias="cq:locked_by", default=None)
    
    new_state: typing.Optional[str] = Field(alias="cq:new_state", default=None)
    
    old_state: typing.Optional[str] = Field(alias="cq:old_state", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    user_name: typing.Optional[str] = Field(alias="cq:user_name", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777224"

    @classmethod
    def shape_name(cls) -> str:
        return "history"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class HistoryProperty(Enum):
    action_name = "cq:action_name"
    action_timestamp = "cq:action_timestamp"
    comments = "cq:comments"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    description = "dcterms:description"
    entity_dbid = "cq:entity_dbid"
    entitydef_id = "cq:entitydef_id"
    entitydef_name = "cq:entitydef_name"
    expired_timestamp = "cq:expired_timestamp"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    new_state = "cq:new_state"
    old_state = "cq:old_state"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    title = "dcterms:title"
    user_name = "cq:user_name"
    version = "cq:version"
    

    def reformat(self, value) -> str:
            if isinstance(value, Resource):
                return f"<{value.uri}>"
            if isinstance(value, datetime):
                dt = value.astimezone(timezone.utc)
                return f'"{dt.isoformat().replace("+00:00", "Z")}"'
            return json.dumps(value)

    def is_one_of(self, values: typing.List[typing.Any]) -> QueryStatement:
        return QueryStatement("{} in [{}]".format(self.value,",".join(map(lambda x: self.reformat(x), values))))

    def nested(self, value: typing.Union[QueryStatement, list[typing.Union[str, Enum]]]) -> QueryStatement:
        if isinstance(value, QueryStatement):
            return QueryStatement(self.value + "{" + str(value) + "}")
        else:
            return QueryStatement(self.value + "{" + ",".join([str(x) for x in value]) + "}")

    def __eq__(self, other):
            return QueryStatement(f"{self.value}={self.reformat(other)}")

    def __ne__(self, other):
        return QueryStatement(f"{self.value}!={self.reformat(other)}")

    def __lt__(self, other):
        return QueryStatement(f"{self.value}<{self.reformat(other)}")

    def __le__(self, other):
        return QueryStatement(f"{self.value}<={self.reformat(other)}")

    def __gt__(self, other):
        return QueryStatement(f"{self.value}>{self.reformat(other)}")

    def __ge__(self, other):
        return QueryStatement(f"{self.value}>={self.reformat(other)}")

    def __str__(self):
        return self.value

    def __repr__(self):
        return self.value


def create_history_changeset(
    action_name: typing.Optional[typing.Literal["Copy_Record", "Import", "Submit"]] = None,
    action_timestamp: typing.Optional[datetime] = None,
    comments: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    description: typing.Optional[str] = None,
    entity_dbid: typing.Optional[int] = None,
    entitydef_id: typing.Optional[int] = None,
    entitydef_name: typing.Optional[str] = None,
    expired_timestamp: typing.Optional[datetime] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[int] = None,
    new_state: typing.Optional[str] = None,
    old_state: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    title: typing.Optional[str] = None,
    user_name: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> History:
    '''
    Create a History changeset.
    '''
    fields = {}
    if action_name is not None:
        fields["cq:action_name"] = action_name 
    if action_timestamp is not None:
        fields["cq:action_timestamp"] = action_timestamp 
    if comments is not None:
        fields["cq:comments"] = comments 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if description is not None:
        fields["dcterms:description"] = description 
    if entity_dbid is not None:
        fields["cq:entity_dbid"] = entity_dbid 
    if entitydef_id is not None:
        fields["cq:entitydef_id"] = entitydef_id 
    if entitydef_name is not None:
        fields["cq:entitydef_name"] = entitydef_name 
    if expired_timestamp is not None:
        fields["cq:expired_timestamp"] = expired_timestamp 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if new_state is not None:
        fields["cq:new_state"] = new_state 
    if old_state is not None:
        fields["cq:old_state"] = old_state 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if title is not None:
        fields["dcterms:title"] = title 
    if user_name is not None:
        fields["cq:user_name"] = user_name 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "history"
    return History(**fields)