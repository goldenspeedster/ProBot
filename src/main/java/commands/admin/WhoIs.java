package commands.admin;

import commands.Command;
import main.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * @author Patrick Ubelhor
 * @version 06/08/2018
 */
public class WhoIs extends Command {

	public WhoIs(Permission perm) {
		super("whois", perm);
	}


	@Override
	public void run(MessageReceivedEvent event, String[] args) {
		TextChannel channel = event.getMessage().getTextChannel();
		List<Member> members = event.getMessage().getMentionedMembers();
		
		// Make sure the user entered at least one @mention
		if (members.isEmpty()) {
			channel.sendMessage(getUsage()).queue();
			return;
		}
		
		// String builder for final message. Format string for each users' information
		StringBuilder sb = new StringBuilder();
		String format =
				"Effective name: %s\n" +
				"ID: %s\n" +
				"Name: %s\n" +
				"Nickname: %s\n" +
				"Joined Discord: %s\n" +
				"Joined guild: %s\n" +
				"Online status: %s\n" +
				"Avatar URL: %s\n" +
				"Roles: %s\n";
		
		// Retrieve the information for each user @mentioned and append to msg string
		for (Member member : members) {
			User user = member.getUser();
			
			String msg = String.format(format,
					member.getEffectiveName(),
					user.getId(),
					user.getName(),
					member.getNickname(),
					user.getCreationTime().toString(),
					member.getJoinDate().toString(),
					member.getOnlineStatus().getKey(),
					user.getEffectiveAvatarUrl(),
					rolesToString(member.getRoles())
			);
			
			sb.append(msg);
			sb.append("\n");
		}
		
		// Finally send the message
		channel.sendMessage(sb.toString()).queue();
	}
	
	
	/**
	 * Converts a list of roles into a comma-space separated string.
	 *
	 * @param roles A list of roles.
	 * @return A string representing the list of roles.
	 */
	private String rolesToString(List<Role> roles) {
		StringBuilder sb = new StringBuilder();
		
		for (Role role : roles) {
			sb.append(role.getName());
			sb.append(", ");
		}
		
		return sb.toString();
	}
	

	@Override
	public String getUsage() {
		return "who @user1 @user2 ...";
	}
	

	@Override
	public String getDescription() {
		return "Gives information about the target user";
	}
}
