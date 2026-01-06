import time

def log_call(func):
    """
    Aspect: Logging before function execution.
    
    Adds a log entry each time the decorated function is called.
    Useful for debugging and runtime monitoring.
    """
    def wrapper(*args, **kwargs):
        print(f"[LOG] Calling {func.__name__}")
        return func(*args, **kwargs)
    return wrapper


def timeit(func):
    """
    Aspect: Execution time measurement.
    
    Measures how long the decorated function takes to run
    using millisecond precision (MicroPython ticks_ms).
    
    Prints: "[TIME] <function> took Xms"
    """
    def wrapper(*args, **kwargs):
        start = time.ticks_ms()
        result = func(*args, **kwargs)
        duration = time.ticks_diff(time.ticks_ms(), start)
        print(f"[TIME] {func.__name__} took {duration}ms")
        return result
    return wrapper


def retry(times=3, delay=200):
    """
    Aspect: Automatic retry on failure.
    
    Retries the decorated function if it raises an Exception.
    
    Args:
        times (int): Number of retry attempts.
        delay (int): Delay between attempts in milliseconds.
    
    Raises:
        Exception: If all attempts fail.
    """
    def decorator(func):
        def wrapper(*args, **kwargs):
            for attempt in range(times):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    print(f"[RETRY] {func.__name__} failed: {e}")
                    time.sleep_ms(delay)
            raise Exception(f"{func.__name__} failed after {times} retries")
        return wrapper
    return decorator
