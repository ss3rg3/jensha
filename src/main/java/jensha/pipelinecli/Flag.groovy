package jensha.pipelinecli

class Flag extends Argument<Boolean> {

    public Boolean value = false

    Flag(List<String> keys, String help) {
        super(keys, help)
    }

    @Override
    Boolean getValue() {
        return this.value
    }
/**
     * Default is false
     */
    void setToTrue() {
        this.value = true
    }

}
