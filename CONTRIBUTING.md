# Contributing to surf-MCCoroutine

surf-MCCoroutine is a patch-based fork of [MCCoroutine](https://github.com/Shynixn/MCCoroutine).
Upstream lives in the `upstream/` git submodule; our changes are maintained as a series of
patch files in `patches/` that are applied on top of it with
[gitpatcher](https://github.com/stellardrift/gitpatcher). The patched sources end up in
`MCCoroutine/` (git-ignored), which is a regular git repository — one commit per patch.

## Getting started

```bash
git clone --recurse-submodules <this repo>
./gradlew applyPatches
```

This checks out the upstream submodule and applies all patches, creating the `MCCoroutine/`
directory. Never edit `patches/*.patch` by hand — they are generated.

## Making changes

1. Run `./gradlew applyPatches` (if you haven't already).
2. Make your changes inside `MCCoroutine/` and commit them there:
   * **One feature, fix, or refactoring per commit.** Each commit becomes one patch file.
   * To change an *existing* patch, edit its commit with `git rebase -i` inside `MCCoroutine/`
     instead of stacking a "fix the previous patch" commit on top.
3. Regenerate the patch files:
   ```bash
   ./gradlew makePatches
   ```
4. Commit the updated `patches/` in the root repository.

### Editing an existing patch

Every patch is exactly one commit in `MCCoroutine/`, so "editing patch NNNN" means
amending that commit. Do **not** stack a "fix the previous patch" commit on top — fold the
change into the original commit with an autosquash rebase:

```bash
# Inside MCCoroutine/
git status                       # make sure the tree is clean except for your change
git add <changed files>          # stage ONLY what belongs in this patch

# Find the commit that backs the patch (the subject matches the patch file name):
git log --oneline                # e.g. 8475108 Rework the Gradle build for the surf fork

# Attach your staged change as a fixup to that commit, then squash it in:
git commit --fixup=8475108
git rebase -i --autosquash 8475108~1
#   The todo list opens with the fixup already placed right after its target and marked
#   `fixup` — just save and close the editor. Nothing else needs changing.
```

Then regenerate and commit the patches from the root repository:

```bash
./gradlew makePatches            # only the affected patch file changes
git add patches/ && git commit
```

Notes:

* The rebase rewrites the commit hashes of every patch after the edited one. That is
  expected — `makePatches` normalises the hashes, so only the patch file you actually
  changed shows up as modified.
* The working tree must be clean apart from the change you are folding in. Commit or
  `git stash` anything unrelated (e.g. a stray `gradlew` file-mode change) first, otherwise
  the rebase aborts with "You have unstaged changes".
* Non-interactive shells (CI, scripts) can skip the editor with
  `GIT_SEQUENCE_EDITOR=: git rebase -i --autosquash 8475108~1`.
* To change more than just the commit's diff (e.g. rewrite its message), use
  `git rebase -i 8475108~1` and mark the commit `edit` / `reword` instead of using a fixup.

## Patch conventions

* Subjects are written in the imperative mood, e.g. `Resolve the Folia dispatchers eagerly`.
* The body explains **what** changed and **why** — patches are our long-term documentation
  of every deviation from upstream, so a reader should understand the motivation without
  reading the diff.
* Mark every modification to upstream code with `surf` comments so changes survive rebases
  and are easy to spot:
  ```kotlin
  someUpstreamCall() // surf - short note about the change

  // surf start - short feature description
  ...added or changed block...
  // surf end - short feature description
  ```
* Prefer commenting out upstream code over deleting it where practical — it keeps future
  upstream updates easier to merge.
* Behaviour must stay compatible with upstream unless the patch explicitly exists to change it.

## Updating upstream

1. Bump the `upstream/` submodule to the new upstream commit.
2. Run `./gradlew applyPatches` and resolve any conflicts by fixing up the affected commits
   (`git rebase -i` / `git cherry-pick` inside `MCCoroutine/`).
3. Run `./gradlew makePatches` and commit the submodule bump together with the regenerated
   patches.
