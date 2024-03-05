import js2py
import re

from js2py.internals.simplex import JsException
from py_mini_racer import MiniRacer


class DynamicFractalAnalyzer:
    def __init__(self, *args, **kwargs) -> None:
        self.mini_racer = MiniRacer()
        pass

    def __get_branches(self, script_text: str, use_mini_racer: bool = False):
        if use_mini_racer:
            return self.mini_racer.eval(script_text)
        try:
            return js2py.eval_js(script_text)
        except JsException:
            return js2py.eval_js6(script_text)

    def __remove_context_content(self, script_text: str) -> str:
        script_text = re.sub(r"context\.[^\n]+\n", "", script_text)
        script_text = re.sub(r"\n[^\n]+document\.[^\n]+\n", "", script_text)
        script_text = re.sub(
            r"\s+(const|var|let)\s+([^\s]+)\s*=\s*\w+\.getContext\(\s*['\"]2d[\"']\s*\)[^\n;]*[\n;]",
            r"\1 \2 = {};",
            script_text,
        )
        return script_text

    def __unwrap(self, script_text: str) -> str:
        return script_text[script_text.find("`") + 1 : script_text.rfind("`")].replace(
            "export ", ""
        )

    def load_fractal(self, fractal_script_path: str, is_wrapped: bool = False) -> str:
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = (
            self.__unwrap(fractal_content) if is_wrapped else fractal_content
        )
        fractal_content = self.__remove_context_content(fractal_content)
        branch_data_script = (
            fractal_content
            + "; function getData() { return JSON.stringify(globalResult); } getData();"
        )
        return self.__get_branches(branch_data_script)

    def load_data_from_fractal(
        self, fractal_script_path: str, variable_name: str, is_wrapped: bool = False
    ) -> str:
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = (
            self.__unwrap(fractal_content) if is_wrapped else fractal_content
        )
        fractal_content = self.__remove_context_content(fractal_content)
        branch_data_script = (
            fractal_content
            + "; function getData() { return JSON.stringify("
            + variable_name
            + " ); } getData();"
        )
        return self.__get_branches(branch_data_script)

    def load_fractal_output_only(self, fractal_script_path: str):
        with open(fractal_script_path, "r", encoding="utf-8") as file:
            fractal_content = file.read()
        fractal_content = self.__remove_context_content(fractal_content)
        print(self.__get_branches(fractal_content))

    def test(self):
        self.load_fractal_output_only("example/1/js/platnoJS.js")


if __name__ == "__main__":
    dyn = DynamicFractalAnalyzer()
    dyn.test()
