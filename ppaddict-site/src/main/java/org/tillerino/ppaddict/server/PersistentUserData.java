package org.tillerino.ppaddict.server;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.tillerino.osuApiModel.types.UserId;
import org.tillerino.ppaddict.shared.BeatmapRangeRequest;
import org.tillerino.ppaddict.shared.PpaddictException;
import org.tillerino.ppaddict.shared.PpaddictException.NotLinked;
import org.tillerino.ppaddict.shared.Settings;

public class PersistentUserData {
  public PersistentUserData() {

  }

  public PersistentUserData(PersistentUserData o) {
    if (o.beatmapComments != null) {
      beatmapComments = new TreeSet<String>(o.beatmapComments);
    }
    if (o.lastRequest != null) {
      lastRequest = new BeatmapRangeRequest(o.lastRequest);
    }
    settings = new Settings(o.settings);
    linkedOsuId = o.linkedOsuId;
  }

  @CheckForNull
  public TreeSet<String> getBeatmapComments() {
    return beatmapComments;
  }

  public void setBeatmapComments(@Nonnull TreeSet<String> beatmapComments) {
    this.beatmapComments = beatmapComments;
  }

  @CheckForNull
  public BeatmapRangeRequest getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(BeatmapRangeRequest lastRequest) {
    this.lastRequest = lastRequest;
  }

  /**
   * @return never null. embedded is never null and default constructor also makes this non null
   */
  @Nonnull
  public Settings getSettings() {
    return settings;
  }

  @CheckForNull
  private TreeSet<String> beatmapComments;

  @CheckForNull
  public Comment getBeatMapComment(int id, long mods) {
    TreeSet<String> comments = getBeatmapComments();
    if (comments == null) {
      return null;
    }

    SortedSet<String> tail = comments.tailSet(id + "-" + mods + "-");

    if (tail.isEmpty()) {
      return null;
    }

    String entry = tail.first();
    if (!entry.startsWith(id + "-" + mods + "-")) {
      return null;
    }

    return new Comment(entry.substring(entry.indexOf('-', entry.indexOf('-') + 1) + 1));
  }

  public static class Comment {
    public String text;
    public long date;

    public Comment(String string) {
      int pos = string.indexOf('-');
      date = Long.parseLong(string.substring(0, pos));
      text = string.substring(pos + 1);
    }

    @Override
    public String toString() {
      return date + "-" + text;
    }
  }

  public void putBeatMapComment(int id, long mods, @Nonnull String comment) {
    TreeSet<String> comments = getBeatmapComments();
    if (comments == null) {
      setBeatmapComments(new TreeSet<String>());
    }

    Comment old = getBeatMapComment(id, mods);

    if (old != null) {
      comments.remove(id + "-" + mods + "-" + old);
    }

    if (comment.length() > 0) {
      comments.add(id + "-" + mods + "-" + System.currentTimeMillis() + "-" + comment);
    }
  }

  @Nonnull
  private Settings settings = new Settings();

  public void setSettings(@Nonnull Settings settings) {
    this.settings = settings;
  }

  @CheckForNull
  private BeatmapRangeRequest lastRequest;

  private @UserId Integer linkedOsuId = null;

  @CheckForNull
  public @UserId Integer getLinkedOsuId() {
    return linkedOsuId;
  }

  @Nonnull
  public @UserId int getLinkedOsuIdOrThrow() throws NotLinked {
    Integer id = getLinkedOsuId();
    if (id == null) {
      throw new PpaddictException.NotLinked();
    }
    return id;
  }

  public void setLinkedOsuId(@UserId Integer linkedOsuId) {
    this.linkedOsuId = linkedOsuId;
  }
}
