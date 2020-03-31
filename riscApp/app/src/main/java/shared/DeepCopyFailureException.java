package shared;

public class DeepCopyFailureException extends RuntimeException {
  private static final long serialVersionUID = 13L; 
  final Exception exn;

   public DeepCopyFailureException(Exception exn) {

      this.exn = exn;

  }

  public Exception getCause(){

    return exn;

  }
}
