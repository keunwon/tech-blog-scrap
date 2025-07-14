rootProject.name = "tech-blog-scrap"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

module(name = ":storage", path = "storage")

module(name = ":core", path = "core")
module(name = ":domain-core", path = "core/domain")

module(name = ":support", path = "support")
module(name = ":logging-support", path = "support/logging")
module(name = ":monitoring-support", path = "support/monitoring")

module(name = ":batch", path = "batch")
module(name = ":scrap-batch", path = "batch/scrap")

module(name = ":library", path = "library")
module(name = ":tech-blog-scarp", path = "library/tech-blog-scrap")

fun module(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}
