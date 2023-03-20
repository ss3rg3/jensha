package jensha.pipelinecli

class Parameter extends Argument<String> {

    public String value

    Parameter(List<String> keys, String help) {
        super(keys, help)
    }

    @Override
    String getValue() {
        return this.value
    }

    void setValue(String value) {
        this.value = value
    }
}
