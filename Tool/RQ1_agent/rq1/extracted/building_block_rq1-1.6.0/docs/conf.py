# Configuration file for the Sphinx documentation builder.
#
# For the full list of built-in configuration values, see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html

# -- Project information -----------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#project-information

project = "building-block-rq1"
copyright = "2024, Nguyen Duc Toan <nto7hc@bosch.com>"
author = "Nguyen Duc Toan <nto7hc@bosch.com>"

# -- General configuration ---------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#general-configuration

extensions = [
    "sphinx.ext.autodoc",
    "sphinx.ext.napoleon",
    "sphinxcontrib.autodoc_pydantic",
]

templates_path = ["_templates"]
exclude_patterns = ["_build", "Thumbs.db", ".DS_Store"]


# -- Options for HTML output -------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#options-for-html-output

html_theme = "sphinx_rtd_theme"
html_static_path = ["_static"]
html_favicon = "_static/favicon.ico"

# -- autodoc_pydantic config --------------------------------------------------
autodoc_pydantic_model_show_json = False
autodoc_pydantic_model_show_validator_summary = False
autodoc_pydantic_field_list_validators = False
