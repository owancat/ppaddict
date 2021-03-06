package org.tillerino.ppaddict.server;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.tillerino.osuApiModel.OsuApiBeatmap;
import org.tillerino.osuApiModel.types.BeatmapId;
import org.tillerino.osuApiModel.types.BitwiseMods;
import org.tillerino.osuApiModel.types.UserId;
import org.tillerino.ppaddict.server.auth.Credentials;
import org.tillerino.ppaddict.shared.types.PpaddictId;

import tillerino.tillerinobot.BeatmapMeta.PercentageEstimates;
import tillerino.tillerinobot.RecommendationsManager.GivenRecommendation;
import tillerino.tillerinobot.UserDataManager.UserData.BeatmapWithMods;



public interface PpaddictBackend {
  public interface BeatmapData {
    OsuApiBeatmap getBeatmap();

    PercentageEstimates getEstimates();
  }

  /**
   * tries to load user data for the given credentials.
   * 
   * this method may only be called from
   * {@link UserDataServiceImpl#createUserData(javax.servlet.http.HttpServletRequest, Credentials)}.
   * if you want to load user data, use that method!
   * 
   * @param userIdentifier
   * @return null if nothing was persisted
   * @throws SQLException
   */
  @CheckForNull
  PersistentUserData loadUserData(Credentials userIdentifier) throws SQLException;

  /**
   * persist the given user data.
   * 
   * this method may only be called from
   * {@link UserDataServiceImpl#saveUserData(Credentials, PersistentUserData)}. if you want to save
   * user data, use that method!
   * 
   * @param usedIdentifier
   * @param data
   * @throws SQLException
   */
  void saveUserData(@Nonnull Credentials usedIdentifier, @Nonnull PersistentUserData data)
      throws SQLException;

  @CheckForNull
  Credentials resolveCookie(String cookie) throws SQLException;

  @Nonnull
  String createCookie(Credentials userIdentifier) throws SQLException;

  String getLinkString(@PpaddictId String id, String displayName) throws SQLException;

  /**
   * @return may return null if the server is shutting down or if there are errors in the backend
   */
  @CheckForNull
  Map<BeatmapWithMods, BeatmapData> getBeatmaps();

  public List<GivenRecommendation> loadVisibleRecommendations(@UserId int userId)
      throws SQLException;

  void hideRecommendation(@UserId int userId, @BeatmapId int beatmapid, @BitwiseMods long mods)
      throws SQLException;
}
