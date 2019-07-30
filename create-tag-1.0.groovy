import org.kohsuke.args4j.Argument

import com.google.gerrit.extensions.annotations.Export
import com.google.gerrit.extensions.api.GerritApi
import com.google.gerrit.sshd.CommandMetaData
import com.google.gerrit.sshd.SshCommand
import com.google.gerrit.extensions.api.projects.TagInput

import com.google.inject.Inject

@Export("new")
@CommandMetaData(name = "new", description = "Create a new tag")
class CreateTagCommand extends SshCommand {

	@Argument(index = 0, required = true, metaVar = "PROJECT", usage = "name of the project")
	String project

	@Argument(index = 1, required = true, metaVar = "TAG", usage = "name of the tag")
	String tag

	@Argument(index = 2, required = true, metaVar = "SHA1", usage = "SHA1 value for the tag")
	String sha1

	@Inject
	GerritApi api

	@Override
	protected void run() throws Exception {
		try {
			def projectApi = api.projects().name(project)
			def projectInfo = projectApi.get()
			def tagInput = TagInput.newInstance([ref:tag, revision:sha1, message:"Created by super-duper Hong"])
			projectApi.tag(tag).create(tagInput)
			stdout.println("Create a tag $tag with value $sha1 to project $project")
		} catch(all) {
			stderr.println("Oops, something went wrong: $all")
		}
	}
}
