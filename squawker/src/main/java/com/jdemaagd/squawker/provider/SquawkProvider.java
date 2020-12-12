package com.jdemaagd.squawker.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Use Schematic (https://github.com/SimonVT/schematic) library
 * to create a content provider and define URIs for provider
 */
@ContentProvider(
        authority = SquawkProvider.AUTHORITY,
        database = SquawkDatabase.class)
public final class SquawkProvider {

    public static final String AUTHORITY = "com.jdemaagd.squawker.provider.provider";

    @TableEndpoint(table = SquawkDatabase.SQUAWK_MESSAGES)
    public static class SquawkMessages {

        @ContentUri(
                path = "messages",
                type = "vnd.android.cursor.dir/messages",
                defaultSort = SquawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }
}