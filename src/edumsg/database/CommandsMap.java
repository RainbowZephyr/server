/*
EduMsg is made available under the OSI-approved MIT license.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package edumsg.database;

import java.util.HashMap;
import java.util.Map;

import edumsg.database.commands.dm.CreateDmCommand;
import edumsg.database.commands.dm.DeleteConversationCommand;
import edumsg.database.commands.dm.DeleteDmCommand;
import edumsg.database.commands.dm.GetConversationCommand;
import edumsg.database.commands.dm.GetConversationsCommand;
import edumsg.database.commands.dm.MarkAllReadCommand;
import edumsg.database.commands.dm.MarkReadCommand;
import edumsg.database.commands.list.AddMemberCommand;
import edumsg.database.commands.list.CreateListCommand;
import edumsg.database.commands.list.DeleteListCommand;
import edumsg.database.commands.list.DeleteMemberCommand;
import edumsg.database.commands.list.GetListFeedsCommand;
import edumsg.database.commands.list.GetListMembersCommand;
import edumsg.database.commands.list.GetListSubscribersCommand;
import edumsg.database.commands.list.SubscribeCommand;
import edumsg.database.commands.list.UnSubscribeCommand;
import edumsg.database.commands.list.UpdateListCommand;
import edumsg.database.commands.tweet.DeleteTweetCommand;
import edumsg.database.commands.tweet.FavoriteCommand;
import edumsg.database.commands.tweet.GetTweetCommand;
import edumsg.database.commands.tweet.NewTweetCommand;
import edumsg.database.commands.tweet.ReportTweetCommand;
import edumsg.database.commands.tweet.RetweetCommand;
import edumsg.database.commands.tweet.UnFavoriteCommand;
import edumsg.database.commands.tweet.UnRetweetCommand;
import edumsg.database.commands.user.ConfirmFollowCommand;
import edumsg.database.commands.user.FollowCommand;
import edumsg.database.commands.user.FollowersCommand;
import edumsg.database.commands.user.FollowingCommand;
import edumsg.database.commands.user.GetFavoritesCommand;
import edumsg.database.commands.user.GetFeedsCommand;
import edumsg.database.commands.user.GetListMembershipsCommand;
import edumsg.database.commands.user.GetMentionsCommand;
import edumsg.database.commands.user.GetSubscribedListsCommand;
import edumsg.database.commands.user.GetTimelineCommand;
import edumsg.database.commands.user.GetUserCommand;
import edumsg.database.commands.user.GetUsersCommand;
import edumsg.database.commands.user.LoginCommand;
import edumsg.database.commands.user.LogoutCommand;
import edumsg.database.commands.user.RegisterCommand;
import edumsg.database.commands.user.ReportUserCommand;
import edumsg.database.commands.user.UnFollowCommand;
import edumsg.database.commands.user.UnconfirmedFollowersCommand;
import edumsg.database.commands.user.UpdateUserCommand;

public class CommandsMap {
	private static Map<String, Class<?>> cmdMap;

	public static void instantiate() {
		cmdMap = new HashMap<String, Class<?>>();
		cmdMap.put("register", RegisterCommand.class);
		cmdMap.put("follow", FollowCommand.class);
		cmdMap.put("unfollow", UnFollowCommand.class);
		cmdMap.put("confirm_follow", ConfirmFollowCommand.class);
		cmdMap.put("report_user", ReportUserCommand.class);
		cmdMap.put("tweet", NewTweetCommand.class);
		cmdMap.put("delete_tweet", DeleteTweetCommand.class);
		cmdMap.put("report_tweet", ReportTweetCommand.class);
		cmdMap.put("favorite", FavoriteCommand.class);
		cmdMap.put("unfavorite", UnFavoriteCommand.class);
		cmdMap.put("retweet", RetweetCommand.class);
		cmdMap.put("unretweet", UnRetweetCommand.class);
		cmdMap.put("get_users", GetUsersCommand.class);
		cmdMap.put("followers", FollowersCommand.class);
		cmdMap.put("following", FollowingCommand.class);
		cmdMap.put("unconfirmed_followers", UnconfirmedFollowersCommand.class);
		cmdMap.put("update_user", UpdateUserCommand.class);
		cmdMap.put("get_user", GetUserCommand.class);
		cmdMap.put("get_mentions", GetMentionsCommand.class);
		cmdMap.put("get_tweet", GetTweetCommand.class);
		cmdMap.put("timeline", GetTimelineCommand.class);
		cmdMap.put("get_favorites", GetFavoritesCommand.class);
		cmdMap.put("get_feeds", GetFeedsCommand.class);
		cmdMap.put("get_subscribed_lists", GetSubscribedListsCommand.class);
		cmdMap.put("get_list_memberships", GetListMembershipsCommand.class);
		cmdMap.put("login", LoginCommand.class);
		cmdMap.put("logout", LogoutCommand.class);

		cmdMap.put("create_dm", CreateDmCommand.class);
		cmdMap.put("delete_dm", DeleteDmCommand.class);
		cmdMap.put("get_conv", GetConversationCommand.class);
		cmdMap.put("get_convs", GetConversationsCommand.class);
		cmdMap.put("delete_conv", DeleteConversationCommand.class);
		cmdMap.put("mark_conv_read", MarkReadCommand.class);
		cmdMap.put("mark_all_conv_read", MarkAllReadCommand.class);

		cmdMap.put("add_member", AddMemberCommand.class);
		cmdMap.put("create_list", CreateListCommand.class);
		cmdMap.put("delete_list", DeleteListCommand.class);
		cmdMap.put("delete_member", DeleteMemberCommand.class);
		cmdMap.put("list_members", GetListMembersCommand.class);
		cmdMap.put("list_subscribers", GetListSubscribersCommand.class);
		cmdMap.put("subscribe", SubscribeCommand.class);
		cmdMap.put("unsubscribe", UnSubscribeCommand.class);
		cmdMap.put("update_list", UpdateListCommand.class);
		cmdMap.put("get_list_feeds", GetListFeedsCommand.class);
	}

	public static Class<?> queryClass(String cmd) {
		return cmdMap.get(cmd);
	}
}
