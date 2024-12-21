package domain.classify;

import java.util.List;

public interface Model<F, L> {
    void train(List<Instance<F, L>> instances);
    void validate(List<Instance<F, L>> instances);
    void test(List<Instance<F, L>> instances);
}
