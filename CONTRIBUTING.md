# Contribution Guidelines

We will always have a need for developers to help improve Thermos. Just make sure you follow our guidelines.

**Note**: Use the handy ./makepatch.sh script to create patch files for changes between eclipse/Clean and eclipse/cauldron.
Coding and Pull Request Conventions
-----------

* We generally follow the Sun/Oracle coding standards.
* No tabs; use 4 spaces instead.
* No trailing whitespaces.
* No CRLF line endings, LF only; *will be converted automatically by git*
* No 80 column limit or 'weird' midstatement newlines.
* The number of commits in a pull request should be kept to a minimum (squish them into one most of the time - use common sense!).
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Pull requests should be tested (does it compile? AND does it work?) before submission.
* Any major additions should have documentation ready and provided if applicable (this is usually the case).
* Most pull requests should be accompanied by a corresponding GitHub ticket so we can associate commits with GitHub issues (this is primarily for changelog generation on ci.md-5.net).
* Try to follow test driven development where applicable.

If you make changes to or add upstream classes (net.minecraft, net.minecraftforge, cpw.mods.fml, org.bukkit, org.spigotmc) it is mandatory to:

* Make a separate commit adding the new net.minecraft classes (commit message: "Added x for diff visibility" or so).
* Then make further commits with your changes.
* Mark your changes with:
    * 1 line; add a trailing: `// Thermos [- Optional reason]`
    * 2+ lines; add
        * Before: `// Thermos start [- Optional comment]`
        * After: `// Thermos end`
* Keep the diffs to a minimum (*somewhat* important)

Tips to get your pull request accepted
-----------
Making sure you follow the above conventions is important, but just the beginning. Follow these tips to better the chances of your pull request being accepted and pulled.

* Make sure you follow all of our conventions to the letter.
* Make sure your code compiles under Java 7.
* Provide proper JavaDocs where appropriate.
* Provide proper accompanying documentation where appropriate.
* Test your code.
* Make sure to follow coding best practices.
* Provide a test plugin/mod binary and socurce for us to test your code with.
* Your pull request should link to accompanying pull requests.
* The description of your pull request should provide detailed information on the pull along with justification of the changes where applicable.

Credits
-------

* [MCP](http://mcp.ocean-labs.de) - permission to use data to make Thermos.
* [Forge](http://www.minecraftforge.net) - mod support.
* [CraftBukkit](http://bukkit.org) - plugin support.
* [Spigot](http://www.spigotmc.org) - performance optimizations.
