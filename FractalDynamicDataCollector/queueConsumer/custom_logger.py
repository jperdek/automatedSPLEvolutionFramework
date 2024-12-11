import io
import logging
import sys
# Taken from https://stackoverflow.com/questions/19425736/how-to-redirect-stdout-and-stderr-to-logger-in-python


class DefaultStreamHandler(logging.StreamHandler):
    def __init__(self, stream=sys.__stdout__):
        # Use the original sys.__stdout__ to write to stdout
        # for this handler, as sys.stdout will write out to logger.
        super().__init__(stream)


class LoggerWriter(io.IOBase):
    """Class to replace the stderr/stdout calls to a logger"""

    def __init__(self, logger_name: str, log_level: int):
        """:param logger_name: Name to give the logger (e.g. 'stderr')
        :param log_level: The log level, e.g. logging.DEBUG / logging.INFO that
                          the MESSAGES should be logged at.
        """
        self.std_logger = logging.getLogger(logger_name)
        # Get the "root" logger from by its name (i.e. from a config dict or at the bottom of this file)
        #  We will use this to create a copy of all its settings, except the name
        app_logger = logging.getLogger("myAppsLogger")
        [self.std_logger.addHandler(handler) for handler in app_logger.handlers]
        self.std_logger.setLevel(app_logger.level)  # the minimum lvl msgs will show at
        self.level = log_level  # the level msgs will be logged at
        self.buffer = []

    def write(self, msg: str):
        """Stdout/stderr logs one line at a time, rather than 1 message at a time.
        Use this function to aggregate multi-line messages into 1 log call."""
        msg = msg.decode() if issubclass(type(msg), bytes) else msg

        if not msg.endswith("\n"):
            return self.buffer.append(msg)

        self.buffer.append(msg.rstrip("\n"))
        message = "".join(self.buffer)
        self.std_logger.log(self.level, message)
        self.buffer = []


def replace_stderr_and_stdout_with_logger():
    """Replaces calls to sys.stderr -> logger.info & sys.stdout -> logger.error"""
    # To access the original stdout/stderr, use sys.__stdout__/sys.__stderr__
    sys.stdout = LoggerWriter("stdout", logging.INFO)
    sys.stderr = LoggerWriter("stderr", logging.ERROR)
