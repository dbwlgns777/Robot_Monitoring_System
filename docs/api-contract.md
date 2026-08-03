# API contract

## Administrator registration approval

- `GET /api/v1/admin/registration-requests`: list pending signup requests.
- `GET /api/v1/admin/users/roles`: list roles the administrator can assign.
- `POST /api/v1/admin/registration-requests/{id}/approve`: create the approved user with the `roleCode` supplied in the JSON body.
- `POST /api/v1/admin/registration-requests/{id}/reject`: reject the pending request.
- `GET /api/v1/admin/users`: list approved users and their current role.
- `PUT /api/v1/admin/users/{id}/role`: replace an approved user's role with the supplied `roleCode`.

State-changing administrator requests must include the CSRF token returned by `GET /api/v1/auth/csrf`.

All three endpoints require an authenticated `ROLE_ADMIN` session and return the standard
`{ success, data, message }` envelope.

Base `/api/v1`; camelCase JSON; ISO-8601 timestamps; session cookie with `credentials: include`.

All responses use `{ "success": true, "data": ..., "message": null }`; errors use `success:false` and an HTTP error status.

| Purpose | Method/path | Response data |
|---|---|---|
| Login | `POST /auth/login` | Request `{username,password,rememberMe}`; response `{id,username,name,roles}`. `rememberMe=true` keeps the server session for 30 days. |
| Signup | `POST /auth/signup` | `{status:"PENDING"}` |
| Session | `GET /auth/me` | current user |
| Profile | `GET /profile`, `PUT /profile` | Read or update the authenticated user's signup profile fields. |
| Password | `PUT /profile/password` | Verify the current password and store a BCrypt hash of the new password. |
| Dashboard | `GET /dashboard/summary` | KPI/current state summary |
| Snapshot | `GET /realtime/equipment` | `Equipment[]` |
| Detail/trend | `GET /realtime/equipment/{id}[/trend]` | equipment / telemetry |
| Production/downtime/alarms | `GET /analytics/{name}` | filtered summary/event rows |
| Equipment/product | `/equipment`, `/products` | list, POST, PUT, PATCH deactivate |
| Collection | `GET /system/collection-health` | collection health rows |

STOMP endpoint `/ws`; topics `/topic/equipment-status`, `/topic/dashboard-kpi`, `/topic/collection-health`. Quality-unlinked KPI is `{qualityDataLinked:false, ppm:null, oee:null}`. Estimated loss is missed production, not defects.
