import asyncio
import enum
import http
from http import client
import json
import operator
import random
from typing import AbstractSet, Any, Dict, Optional
from urllib import parse

import aiohttp
from aiohttp import hdrs, web
from gidgethub.aiohttp import GitHubAPI
from gidgethub import sansio
import uritemplate

from . import abc as ni_abc

JSON = Any
JSONDict = Dict[str, Any]


LABEL_PREFIX = 'CLA '
CLA_OK = LABEL_PREFIX + 'signed'
NO_CLA = LABEL_PREFIX + 'not signed'
EASTEREGG_PROBABILITY = 0.01

NO_CLA_TEMPLATE = """Hello, and thanks for your contribution!
I'm a bot set up to make sure that the project can legally accept your \
contribution by verifying you have signed the \
[PSF contributor agreement](https://www.python.org/psf/contrib/contrib-form/) \
(CLA).
{body}
Thanks again to your contribution and we look forward to looking at it!
"""

NO_CLA_BODY = """Unfortunately our records indicate you have not signed the CLA. \
For legal reasons we need you to sign this before we can look at your \
contribution. Please follow \
[the steps outlined in the CPython devguide](https://devguide.python.org/pullrequest/#licensing) \
to rectify this issue.
"""

NO_CLA_BODY_EASTEREGG = NO_CLA_BODY + """
We also demand... [A SHRUBBERY!](https://www.youtube.com/watch?v=zIV4poUZAQo)
"""

NO_USERNAME_BODY = """Unfortunately we couldn't find an account corresponding \
to your GitHub username on [bugs.python.org](https://bugs.python.org/) \
(b.p.o) to verify you have signed the CLA (this might be simply due to a \
missing "GitHub Name" entry in your b.p.o account settings). This is necessary \
for legal reasons before we can look at your contribution. Please follow \
[the steps outlined in the CPython devguide](https://devguide.python.org/pullrequest/#licensing) \
to rectify this issue.
"""


GITHUB_EMAIL = 'noreply@github.com'.lower()  # Normalized for easy comparisons.


@enum.unique
class PullRequestEvent(enum.Enum):
    # https://developer.github.com/v3/activity/events/types/#pullrequestevent
    assigned = "assigned"
    unassigned = "unassigned"
    labeled = "labeled"
    unlabeled = "unlabeled"
    opened = "opened"
    closed = "closed"
    reopened = "reopened"
    synchronize = "synchronize"