using System.Net;

namespace app.Dto;

public class ApiResponse<TData, TException>
    where TException : Exception
{
    public TData? Data { get; }
    public TException? Exception { get; }
    public bool IsValid => Exception is null;

    public ApiResponse(TData data)
    {
        Data = data;
    }

    public ApiResponse(TException exception)
    {
        Exception = exception;
    }

    public T Match<T>(
        Func<TData?, T> onSuccess,
        Func<TException?, T> onError
    )
    {
        return IsValid ? onSuccess(Data) : onError(Exception);
    }
}
