package com.xetus.iris.cli

import groovy.json.JsonOutput

import groovy.transform.CompileStatic

import asg.cliche.Command
import asg.cliche.InputConverter
import asg.cliche.Param
import asg.cliche.Shell
import asg.cliche.ShellManageable

import com.xetus.iris.FreeIPAClient

@CompileStatic
class AuthenticatedShell implements ShellManageable {
  
  public static final InputConverter[] CLI_INPUT_CONVERTERS = [
    new MapInputConverter()
  ]
  
  private FreeIPAClient client
  private Shell shell
  
  public AuthenticatedShell(FreeIPAClient client) {
    this.client = client
  }
  
  public void cliSetShell(Shell shell) {
    this.shell == shell
  }
  
  private String format(Object o) {
    return JsonOutput.prettyPrint(JsonOutput.toJson(o))
  }
  
  @Command(description = "calls the JSON RPC user_show method")
  public String userShow(
        @Param(name = "uid", description="UID of the user to show") String uid,
        @Param(name = "params", description="additional method parameters") Map<String, String> params = [:]) {
    return format(client.userShow(uid, params))
  }
  
  @Command(description = "calls the JSON RPC user_find method")
  public String userFind(
        @Param(name = "uid", description="UID of the user to show") String uid = null,
        @Param(name = "params", description="additional method parameters") Map<String, String> params = [:]) {
    return format(client.userFind(uid == "null" ? (List<String>) [] : [uid], params))
  }
  
  @Command(description = "calls the JSON RPC pwpolicy_show method")
  public String pwPolicyShow(
        @Param(name = "params", description="additional parameters") Map<String, String> params = [:]) {
    return format(client.pwpolicyShow(params))
  }
  
  @Command(description = "calls the JSON RPC krbtpolicy_show method")
  public String krbTktPolicyShow(
        @Param(name = "user", description="the user for whom to show the KRB ticket policy") String user = null, 
        @Param(name = "params", description="additional parameters") Map<String, String> params = [:]) {
    user = user == "null" ? null : user
    return format(client.krbtpolicyShow(user, params))
  }
  
  @Command(description = "calls the JSON RPC passwd method")
  public String passwd(
        @Param(name = "user", description="the user's UID for whom to change the password") String user, 
        @Param(name = "oldPass", description="the existing password for the user") String oldPass, 
        @Param(name = "newPass", description="the password to which to change the user's password") String newPass = null) {
    return format((newPass == null) ? 
                   client.passwd(user, oldPass) : 
                   client.passwd(user, oldPass, newPass))
  }
  
  @Command(description = "calls the JSON RPC user_add method")
  public String userAdd(
        @Param(name = "uid", description="UID of the user to add") String uid, 
        @Param(name = "givenName", description="givenName of the user to add") String givenName, 
        @Param(name = "sn", description="sn of the user to add") String sn, 
        @Param(name = "attr", description="additional attributes for the user to add") Map<String, String> attributes = [:]) {
    return format(client.userAdd(uid, givenName, sn, attributes))
  }

  @Override
  public void cliEnterLoop() {}

  @Override
  public void cliLeaveLoop() {
    client.logout()
  }
}
