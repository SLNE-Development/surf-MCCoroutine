plugins {
    id("ca.stellardrift.gitpatcher") version "2.0.0"
}

gitPatcher.patchedRepos {
    register("MCCoroutine") {
        submodule = "upstream"
        target = file("MCCoroutine")
        patches = file("patches")
    }
}