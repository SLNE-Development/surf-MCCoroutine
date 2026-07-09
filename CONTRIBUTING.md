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
