package org.tillerino.ppaddict.client.services;

import org.tillerino.ppaddict.shared.ClientUserData;
import org.tillerino.ppaddict.shared.InitialData;
import org.tillerino.ppaddict.shared.PpaddictException;
import org.tillerino.ppaddict.shared.Settings;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(value = "user")
public interface UserDataService extends RemoteService {
  ClientUserData getStatus() throws PpaddictException;

  void saveSettings(Settings s) throws PpaddictException;

  InitialData getInitialData() throws PpaddictException;

  String getLinkString() throws PpaddictException;

  void saveComment(int beatmapid, String mods, String comment) throws PpaddictException;
}
