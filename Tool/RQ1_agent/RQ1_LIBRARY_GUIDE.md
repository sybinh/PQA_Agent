# Building-Block-RQ1 Library - Tài li?u T?ng Quan

## ?? Gi?i Thi?u

`building-block-rq1` là th? vi?n Python chính th?c c?a Bosch ?? làm vi?c v?i h? th?ng RQ1/ClearQuest. Version hi?n t?i: **1.6.0**

## ?? Cài ??t

```bash
# ?ã cài ??t t? wheel file local
.\venv\Scripts\python.exe -m pip install .\rq1\building_block_rq1-1.6.0-py3-none-any.whl
```

## ?? C?u Trúc Chính

### 1. Client Classes

#### **Client** (Sync)
```python
from rq1 import Client, BaseUrl

client = Client(
    base_url=BaseUrl.PRODUCTIVE,  # ho?c ACCEPTANCE, APPROVAL
    username=os.environ["RQ1_USER"],
    password=os.environ["RQ1_PASSWORD"],
    toolname=os.environ["RQ1_TOOLNAME"],
    toolversion=os.environ["RQ1_TOOLVERSION"],
)
```

#### **AsyncClient** (Async)
```python
from rq1 import AsyncClient, BaseUrl

async with AsyncClient(
    base_url=BaseUrl.PRODUCTIVE,
    username=...,
    password=...,
    toolname=...,
    toolversion=...,
) as client:
    # async operations
```

### 2. Base URLs

```python
from rq1 import BaseUrl

BaseUrl.PRODUCTIVE   # Production environment
BaseUrl.ACCEPTANCE   # Test environment
BaseUrl.APPROVAL     # Approval environment
```

### 3. Record Types (Models)

Library cung c?p các model ?? làm vi?c v?i RQ1:

- **Issue** - RQ1 issues/tickets
- **Workitem** - Work items
- **Project** - Projects
- **Users** - User records
- **Release** - Releases
- **Problem** - Problems
- **Contact** - Contacts
- **Groups** - Groups
- **History** - History records
- Và nhi?u lo?i khác...

## ?? Các Ch?c N?ng Chính

### 1. Get Record (L?y Record)

#### Get by URI
```python
from rq1.models import Workitem

wi = client.get_record_by_uri(
    Workitem,
    "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/record/16777232-37320999"
)
```

#### Get by RQ1 Number
```python
from rq1.models import Issue, IssueProperty

# L?y t?t c? properties (ch?m)
issue = client.get_record_by_rq1_number(Issue, "RQONE03765304")

# Ch? l?y properties c?n thi?t (nhanh h?n)
issue = client.get_record_by_rq1_number(
    Issue, 
    "RQONE03765304", 
    select=[IssueProperty.accountnumbers, IssueProperty.headline]
)
```

### 2. Query Records (Tìm Ki?m Records)

#### Simple Query - L?y t?t c?
```python
from rq1.models import Workitem

# Query t?t c? workitems
query = client.query(Workitem, select="*", paging=True, page_size=5)

print(f"Total: {query.total_count}")
print(f"Members in page: {len(query.members)}")

# L?y trang ti?p theo
next_page = client.get_next_query_page(query)
```

#### Complex Query - V?i ?i?u ki?n
```python
from rq1.models import Issue, IssueProperty, Project, ProjectProperty
from rq1.base import reference
from datetime import datetime

# Tìm project
project_query = client.query(
    Project,
    where=ProjectProperty.id == "RQONE00002140",
    select=[ProjectProperty.dcterms__title, ProjectProperty.status]
)
project = project_query.members[0]

# Query issues v?i nhi?u ?i?u ki?n
clause_1 = IssueProperty.belongstoproject == reference(project.uri)
clause_2 = IssueProperty.submitdate > datetime(2020, 1, 1)
clause_3 = IssueProperty.priority == "High"

query = client.query(
    Issue, 
    where=clause_1 & clause_2 & clause_3,  # Combine v?i &
    paging=True, 
    page_size=10
)
```

#### Run Predefined Query
```python
# Ch?y query ?ã ???c ??nh ngh?a s?n trên RQ1 web
query = client.run_query(
    Workitem, 
    query_id=192940459, 
    select="*", 
    page_size=10
)
```

### 3. Query Operators

```python
from rq1.models import IssueProperty

# So sánh
IssueProperty.priority == "High"
IssueProperty.submitdate > datetime(2020, 1, 1)
IssueProperty.submitdate < datetime(2024, 12, 31)

# Combine v?i & (AND) ho?c | (OR)
clause = (IssueProperty.priority == "High") & (IssueProperty.status == "Open")

# is_one_of - Ki?m tra trong danh sách
IssueProperty.assignee.is_one_of([reference(user1.uri), reference(user2.uri)])
```

### 4. Pagination (Phân Trang)

```python
# Enable paging
query = client.query(Issue, select="*", paging=True, page_size=10)

# Trang ti?p theo
next_page = client.get_next_query_page(query)

# Trang ? v? trí b?t k? (start_index là member index, không ph?i page index)
page = client.get_query_page(query, start_index=20)
```

## ?? Các Model Quan Tr?ng

### Issue (Ticket)

```python
from rq1.models import Issue, IssueProperty

# Properties có s?n:
IssueProperty.id                  # RQ1 ID
IssueProperty.headline            # Tiêu ??
IssueProperty.state               # Tr?ng thái
IssueProperty.priority            # ?u tiên
IssueProperty.assignee            # Ng??i ???c assign
IssueProperty.submitdate          # Ngày t?o
IssueProperty.belongstoproject    # Project
IssueProperty.accountnumbers      # Account numbers
IssueProperty.description         # Mô t?
# ... và nhi?u properties khác
```

### Workitem

```python
from rq1.models import Workitem, WorkitemProperty

# Properties
WorkitemProperty.id
WorkitemProperty.headline
WorkitemProperty.state
# ...
```

### Project

```python
from rq1.models import Project, ProjectProperty

ProjectProperty.id
ProjectProperty.dcterms__title    # Title
ProjectProperty.status
# ...
```

### Users

```python
from rq1.models import Users, UsersProperty

UsersProperty.login_name
UsersProperty.fullname
UsersProperty.email
# ...
```

## ?? Cách S? D?ng Reference

Khi c?n tham chi?u ??n m?t record khác (foreign key):

```python
from rq1.base import reference

# L?y URI c?a project
project_uri = project.uri

# S? d?ng reference trong query
IssueProperty.belongstoproject == reference(project_uri)
```

## ?? Query Result Structure

```python
query = client.query(Issue, ...)

# Thu?c tính c?a QueryResult:
query.members        # List[Issue] - Danh sách records
query.total_count    # int - T?ng s? records
query.next_page      # str - URL c?a trang ti?p
query.rtype          # type - Record type (Issue, Workitem, etc.)
```

## ?? Best Practices

### 1. **Ch? l?y properties c?n thi?t**
```python
# ? Ch?m - l?y t?t c?
issue = client.get_record_by_rq1_number(Issue, "RQONE03765304")

# ? Nhanh - ch? l?y c?n thi?t
issue = client.get_record_by_rq1_number(
    Issue, 
    "RQONE03765304",
    select=[IssueProperty.headline, IssueProperty.state]
)
```

### 2. **S? d?ng paging cho query l?n**
```python
# ? T?t - dùng paging
query = client.query(Issue, select="*", paging=True, page_size=50)
```

### 3. **S? d?ng AsyncClient cho nhi?u requests**
```python
async def get_multiple_issues(issue_ids):
    async with AsyncClient(...) as client:
        tasks = [
            client.get_record_by_rq1_number(Issue, issue_id)
            for issue_id in issue_ids
        ]
        return await asyncio.gather(*tasks)
```

## ?? Authentication

Environment variables c?n thi?t:
```bash
RQ1_USER         # Username
RQ1_PASSWORD     # Password
RQ1_TOOLNAME     # Tool name (?? tracking)
RQ1_TOOLVERSION  # Tool version (?? tracking)
```

## ?? Tài Li?u Tham Kh?o

- **Official Docs**: https://pages.github.boschdevcloud.com/bios-automation-marketplace/building-block-rq1/
- **GitHub Repo**: https://github.boschdevcloud.com/bios-automation-marketplace/building-block-rq1
- **Examples**: Trong folder `rq1/extracted/building_block_rq1-1.6.0/examples/`

## ?? Các Ví D? Hoàn Ch?nh

Xem các file trong folder examples:
- `get.py` - L?y records
- `query.py` - Query v?i ?i?u ki?n
- `create.py` - T?o records m?i
- `modify.py` - S?a records
- `async_example.py` - S? d?ng AsyncClient
- `attachments.py` - Làm vi?c v?i attachments

## ?? Notes Quan Tr?ng

1. **SSL Verification**: Library t? ??ng disable SSL warnings
2. **Tracking**: Library t? ??ng g?i usage tracking (tr? CI/CD environment)
3. **Session Management**: Client t? ??ng qu?n lı session v?i RQ1
4. **Base URL**: Có 3 environments - PRODUCTIVE, ACCEPTANCE, APPROVAL

---

**Next Steps**: 
- Bây gi? chúng ta có th? update `rq1_client.py` ?? s? d?ng library này
- Ho?c vi?t tr?c ti?p trong `rq1_server.py` mà không c?n wrapper
