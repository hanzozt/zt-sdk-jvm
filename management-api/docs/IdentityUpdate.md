

# IdentityUpdate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**appData** | [**Tags**](Tags.md) |  |  [optional] |
|**authPolicyId** | **String** |  |  [optional] |
|**defaultHostingCost** | **Integer** |  |  [optional] |
|**defaultHostingPrecedence** | **TerminatorPrecedence** |  |  [optional] |
|**externalId** | **String** |  |  [optional] |
|**isAdmin** | **Boolean** |  |  |
|**name** | **String** |  |  |
|**permissions** | [**List&lt;PermissionsEnum&gt;**](#List&lt;PermissionsEnum&gt;) | A set of strings used to define which types of resources this resource can acccess |  [optional] |
|**roleAttributes** | **List&lt;String&gt;** | A set of strings used to loosely couple this resource to policies |  [optional] |
|**serviceHostingCosts** | **Map&lt;String, Integer&gt;** |  |  [optional] |
|**serviceHostingPrecedences** | **Map&lt;String, TerminatorPrecedence&gt;** |  |  [optional] |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**type** | **IdentityType** |  |  |



## Enum: List&lt;PermissionsEnum&gt;

| Name | Value |
|---- | -----|
| ADMIN_READONLY | &quot;admin_readonly&quot; |
| AUTH_POLICY | &quot;auth-policy&quot; |
| AUTH_POLICY_READ | &quot;auth-policy.read&quot; |
| AUTH_POLICY_CREATE | &quot;auth-policy.create&quot; |
| AUTH_POLICY_UPDATE | &quot;auth-policy.update&quot; |
| AUTH_POLICY_DELETE | &quot;auth-policy.delete&quot; |
| CA | &quot;ca&quot; |
| CA_READ | &quot;ca.read&quot; |
| CA_CREATE | &quot;ca.create&quot; |
| CA_UPDATE | &quot;ca.update&quot; |
| CA_DELETE | &quot;ca.delete&quot; |
| CONFIG | &quot;config&quot; |
| CONFIG_READ | &quot;config.read&quot; |
| CONFIG_CREATE | &quot;config.create&quot; |
| CONFIG_UPDATE | &quot;config.update&quot; |
| CONFIG_DELETE | &quot;config.delete&quot; |
| CONFIG_TYPE | &quot;config-type&quot; |
| CONFIG_TYPE_READ | &quot;config-type.read&quot; |
| CONFIG_TYPE_CREATE | &quot;config-type.create&quot; |
| CONFIG_TYPE_UPDATE | &quot;config-type.update&quot; |
| CONFIG_TYPE_DELETE | &quot;config-type.delete&quot; |
| EDGE_ROUTER_POLICY | &quot;edge-router-policy&quot; |
| EDGE_ROUTER_POLICY_READ | &quot;edge-router-policy.read&quot; |
| EDGE_ROUTER_POLICY_CREATE | &quot;edge-router-policy.create&quot; |
| EDGE_ROUTER_POLICY_UPDATE | &quot;edge-router-policy.update&quot; |
| EDGE_ROUTER_POLICY_DELETE | &quot;edge-router-policy.delete&quot; |
| ENROLLMENT | &quot;enrollment&quot; |
| ENROLLMENT_READ | &quot;enrollment.read&quot; |
| ENROLLMENT_CREATE | &quot;enrollment.create&quot; |
| ENROLLMENT_UPDATE | &quot;enrollment.update&quot; |
| ENROLLMENT_DELETE | &quot;enrollment.delete&quot; |
| EXTERNAL_JWT_SIGNER | &quot;external-jwt-signer&quot; |
| EXTERNAL_JWT_SIGNER_READ | &quot;external-jwt-signer.read&quot; |
| EXTERNAL_JWT_SIGNER_CREATE | &quot;external-jwt-signer.create&quot; |
| EXTERNAL_JWT_SIGNER_UPDATE | &quot;external-jwt-signer.update&quot; |
| EXTERNAL_JWT_SIGNER_DELETE | &quot;external-jwt-signer.delete&quot; |
| IDENTITY | &quot;identity&quot; |
| IDENTITY_READ | &quot;identity.read&quot; |
| IDENTITY_CREATE | &quot;identity.create&quot; |
| IDENTITY_UPDATE | &quot;identity.update&quot; |
| IDENTITY_DELETE | &quot;identity.delete&quot; |
| OPS | &quot;ops&quot; |
| OPS_READ | &quot;ops.read&quot; |
| OPS_UPDATE | &quot;ops.update&quot; |
| OPS_DELETE | &quot;ops.delete&quot; |
| POSTURE_CHECK | &quot;posture-check&quot; |
| POSTURE_CHECK_READ | &quot;posture-check.read&quot; |
| POSTURE_CHECK_CREATE | &quot;posture-check.create&quot; |
| POSTURE_CHECK_UPDATE | &quot;posture-check.update&quot; |
| POSTURE_CHECK_DELETE | &quot;posture-check.delete&quot; |
| ROUTER | &quot;router&quot; |
| ROUTER_READ | &quot;router.read&quot; |
| ROUTER_CREATE | &quot;router.create&quot; |
| ROUTER_UPDATE | &quot;router.update&quot; |
| ROUTER_DELETE | &quot;router.delete&quot; |
| SERVICE | &quot;service&quot; |
| SERVICE_READ | &quot;service.read&quot; |
| SERVICE_CREATE | &quot;service.create&quot; |
| SERVICE_UPDATE | &quot;service.update&quot; |
| SERVICE_DELETE | &quot;service.delete&quot; |
| SERVICE_EDGE_ROUTER_POLICY | &quot;service-edge-router-policy&quot; |
| SERVICE_EDGE_ROUTER_POLICY_READ | &quot;service-edge-router-policy.read&quot; |
| SERVICE_EDGE_ROUTER_POLICY_CREATE | &quot;service-edge-router-policy.create&quot; |
| SERVICE_EDGE_ROUTER_POLICY_UPDATE | &quot;service-edge-router-policy.update&quot; |
| SERVICE_EDGE_ROUTER_POLICY_DELETE | &quot;service-edge-router-policy.delete&quot; |
| SERVICE_POLICY | &quot;service-policy&quot; |
| SERVICE_POLICY_READ | &quot;service-policy.read&quot; |
| SERVICE_POLICY_CREATE | &quot;service-policy.create&quot; |
| SERVICE_POLICY_UPDATE | &quot;service-policy.update&quot; |
| SERVICE_POLICY_DELETE | &quot;service-policy.delete&quot; |
| TERMINATOR | &quot;terminator&quot; |
| TERMINATOR_READ | &quot;terminator.read&quot; |
| TERMINATOR_CREATE | &quot;terminator.create&quot; |
| TERMINATOR_UPDATE | &quot;terminator.update&quot; |
| TERMINATOR_DELETE | &quot;terminator.delete&quot; |



