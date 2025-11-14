
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .historylog import Historylog

    from .users import Users



class Contact(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    activecontact: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:ActiveContact", default=None)
    
    collective_email: typing.Optional[str] = Field(alias="cq:Collective_eMail", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    department: typing.Optional[str] = Field(alias="cq:Department", default=None)
    
    email: typing.Optional[str] = Field(alias="cq:eMail", default=None)
    
    firstname: typing.Optional[str] = Field(alias="cq:FirstName", default=None)
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastname: typing.Optional[str] = Field(alias="cq:LastName", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    organization: typing.Optional[str] = Field(alias="cq:Organization", default=None)
    
    phonenumbers: typing.Optional[str] = Field(alias="cq:PhoneNumbers", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777236"

    @classmethod
    def shape_name(cls) -> str:
        return "Contact"

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
    


class ContactProperty(Enum):
    activecontact = "cq:ActiveContact"
    collective_email = "cq:Collective_eMail"
    cq__Description = "cq:Description"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    department = "cq:Department"
    email = "cq:eMail"
    firstname = "cq:FirstName"
    hashistorylogs = "cq:hasHistoryLogs"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lastname = "cq:LastName"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    name = "cq:Name"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    organization = "cq:Organization"
    phonenumbers = "cq:PhoneNumbers"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
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


def create_contact_changeset(
    activecontact: typing.Optional[typing.Literal["No", "Yes"]] = None,
    collective_email: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    department: typing.Optional[str] = None,
    email: typing.Optional[str] = None,
    firstname: typing.Optional[str] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lastname: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    name: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    organization: typing.Optional[str] = None,
    phonenumbers: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Contact:
    '''
    Create a Contact changeset.
    '''
    fields = {}
    if activecontact is not None:
        fields["cq:ActiveContact"] = activecontact 
    if collective_email is not None:
        fields["cq:Collective_eMail"] = collective_email 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if department is not None:
        fields["cq:Department"] = department 
    if email is not None:
        fields["cq:eMail"] = email 
    if firstname is not None:
        fields["cq:FirstName"] = firstname 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if history is not None:
        fields["cq:history"] = history 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lastname is not None:
        fields["cq:LastName"] = lastname 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if name is not None:
        fields["cq:Name"] = name 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if organization is not None:
        fields["cq:Organization"] = organization 
    if phonenumbers is not None:
        fields["cq:PhoneNumbers"] = phonenumbers 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Contact"
    return Contact(**fields)