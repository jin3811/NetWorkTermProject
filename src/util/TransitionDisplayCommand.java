package util;

/**
 * 기능
 * JPanel을 전환하면서 화면을 변환하는 시점에서 추가적으로 해야하는 일을 정의한다.
 * 예를 들어 화면의 크기를 바꾼다던가 하는 등
 */
public interface TransitionDisplayCommand {
    public void execute();
}
