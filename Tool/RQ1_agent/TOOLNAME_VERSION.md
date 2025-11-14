# RQ1 Tool Name and Version Management

## T?ng quan

RQ1 system yêu c?u t?t c? client tools ph?i ??ng ký và g?i **toolname** và **toolversion** trong m?i request thông qua HTTP header `x-requester`.

## Cách ho?t ??ng

### 1. Header Format
```
x-requester: toolname=rq1-mcp-server;toolversion=0.1.0;user=username
```

### 2. Whitelist Requirement
- ?? **QUAN TR?NG**: `toolname` ph?i ???c ??ng ký và whitelist b?i RQ1 administrators
- N?u toolname không ???c whitelist, requests s? b? t? ch?i
- Contact RQ1 admins ?? ??ng ký tool m?i

### 3. Auto-Detection

Tool t? ??ng detect version t? code:

```python
# rq1_client.py
__version__ = "0.1.0"  # Update này khi release version m?i
DEFAULT_TOOLNAME = "rq1-mcp-server"
DEFAULT_TOOLVERSION = __version__
```

### 4. Configuration Priority

Tool name và version ???c xác ??nh theo th? t? ?u tiên:

1. **Environment Variables** (highest priority):
   ```bash
   RQ1_TOOLNAME=my-custom-tool
   RQ1_TOOLVERSION=1.2.3
   ```

2. **Code Defaults** (fallback):
   ```python
   DEFAULT_TOOLNAME = "rq1-mcp-server"
   DEFAULT_TOOLVERSION = "0.1.0"
   ```

## Setup Guide

### Option 1: S? d?ng Default (Recommended)

N?u b?n ?ã ??ng ký tool v?i tên `"rq1-mcp-server"`:

```bash
# .env - Ch? c?n user/password
RQ1_USER=your_username
RQ1_PASSWORD=your_password
# RQ1_TOOLNAME và RQ1_TOOLVERSION s? t? ??ng dùng defaults
```

### Option 2: Custom Tool Name

N?u b?n có toolname riêng ?ã ???c whitelist:

```bash
# .env
RQ1_USER=your_username
RQ1_PASSWORD=your_password
RQ1_TOOLNAME=my-registered-tool
RQ1_TOOLVERSION=1.0.0
```

## ??ng ký Tool m?i

?? ??ng ký toolname m?i v?i RQ1:

1. **Contact RQ1 Administrators**
   - Email ho?c ticket yêu c?u whitelist tool
   - Cung c?p:
     - Tool name (e.g., "my-awesome-tool")
     - Purpose/Description
     - Expected usage volume

2. **Ch? Approval**
   - RQ1 admins s? review và whitelist tool
   - Th??ng m?t vài ngày

3. **Configure sau khi approved**
   ```bash
   RQ1_TOOLNAME=my-approved-toolname
   RQ1_TOOLVERSION=1.0.0
   ```

## Version Numbering

Nên follow **Semantic Versioning**:

- `MAJOR.MINOR.PATCH`
- Ví d?: `1.2.3`
  - `1` = Major version (breaking changes)
  - `2` = Minor version (new features)
  - `3` = Patch version (bug fixes)

Khi release version m?i:

1. Update `__version__` trong `rq1_client.py`:
   ```python
   __version__ = "0.2.0"  # New version
   ```

2. Update `pyproject.toml`:
   ```toml
   [project]
   version = "0.2.0"
   ```

3. Optional: Override trong `.env`:
   ```bash
   RQ1_TOOLVERSION=0.2.0
   ```

## Troubleshooting

### Error: "Tool not authorized"

**Nguyên nhân**: Toolname ch?a ???c whitelist

**Gi?i pháp**:
1. Verify toolname ?úng: Check `.env` ho?c defaults
2. Contact RQ1 admins ?? whitelist
3. Temporary: Dùng toolname ?ã ???c approve (n?u có access)

### Error: "Invalid version format"

**Nguyên nhân**: Version format không ?úng

**Gi?i pháp**: Dùng format `X.Y.Z` (e.g., `1.0.0`, `0.1.0`)

## Best Practices

1. ? **Dùng defaults n?u có th?** - ??n gi?n h?n
2. ? **Version theo Semantic Versioning** - D? track changes
3. ? **Document version changes** - Maintain CHANGELOG
4. ? **Test v?i ACCEPTANCE tr??c** - Verify tr??c khi production
5. ?? **Không hardcode credentials** - Luôn dùng environment variables

## Reference

- RQ1 Library: `building-block-rq1` v1.6.0
- Header spec: `x-requester` format defined by RQ1 OSLC API
- Semantic Versioning: https://semver.org/
