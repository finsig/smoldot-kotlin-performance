/**
 * A Chain Specification asset file.
 *
 * @property fileName the name of the asset file.
 */
enum class ChainSpecificationAsset(val fileName: String) {
    POLKADOT("polkadot.json"),
    KUSAMA("kusama.json"),
    ROCOCO("rococo.json"),
    WESTEND("westend.json")
}