
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .issuereleasemap import Issuereleasemap

    from .issue import Issue

    from .users import Users



class Attachmentmapping(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    attribute: typing.Optional[str] = Field(alias="cq:Attribute", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    eng_attachment: typing.Optional[str] = Field(alias="cq:Eng_Attachment", default=None)
    
    exportstate: typing.Optional[str] = Field(alias="cq:ExportState", default=None)
    
    hasissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasIssue", default=None)
    
    hasissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:hasIssuereleasemap", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    linkedid: typing.Optional[str] = Field(alias="cq:linkedId", default=None)
    
    linkedrecordtype: typing.Optional[str] = Field(alias="cq:linkedRecordType", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    sales_attachment: typing.Optional[str] = Field(alias="cq:Sales_Attachment", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777243"

    @classmethod
    def shape_name(cls) -> str:
        return "AttachmentMapping"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class AttachmentmappingProperty(Enum):
    attribute = "cq:Attribute"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    eng_attachment = "cq:Eng_Attachment"
    exportstate = "cq:ExportState"
    hasissue = "cq:hasIssue"
    hasissuereleasemap = "cq:hasIssuereleasemap"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    linkedid = "cq:linkedId"
    linkedrecordtype = "cq:linkedRecordType"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    sales_attachment = "cq:Sales_Attachment"
    shorttitle = "oslc:shortTitle"
    title = "dcterms:title"
    tld = "cq:TLD"
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


def create_attachmentmapping_changeset(
    attribute: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    eng_attachment: typing.Optional[str] = None,
    exportstate: typing.Optional[str] = None,
    hasissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    hasissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    linkedid: typing.Optional[str] = None,
    linkedrecordtype: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    sales_attachment: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Attachmentmapping:
    '''
    Create a Attachmentmapping changeset.
    '''
    fields = {}
    if attribute is not None:
        fields["cq:Attribute"] = attribute 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if eng_attachment is not None:
        fields["cq:Eng_Attachment"] = eng_attachment 
    if exportstate is not None:
        fields["cq:ExportState"] = exportstate 
    if hasissue is not None:
        fields["cq:hasIssue"] = hasissue 
    if hasissuereleasemap is not None:
        fields["cq:hasIssuereleasemap"] = hasissuereleasemap 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if linkedid is not None:
        fields["cq:linkedId"] = linkedid 
    if linkedrecordtype is not None:
        fields["cq:linkedRecordType"] = linkedrecordtype 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if sales_attachment is not None:
        fields["cq:Sales_Attachment"] = sales_attachment 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "AttachmentMapping"
    return Attachmentmapping(**fields)