import js2py
import re
import requests

from js2py.internals.simplex import JsException
from py_mini_racer import MiniRacer


class DynamicFractalAnalyzer:
    def __init__(self, *args, **kwargs) -> None:
        self.mini_racer = MiniRacer()
        pass

    def __process_transpiled_script_for_js_to_py(self, script_text: str) -> str:
        script_text_array = script_text.split("/*")
        whole_text = ""
        for script_text_part in script_text_array:
            text_part = script_text_part.split("*/")
            if len(text_part) == 1:
                whole_text = whole_text + text_part[0]
            else:
                whole_text = whole_text + text_part[1]
        script_text = whole_text
        return script_text

    def __get_branches(
        self,
        script_text: str,
        use_mini_racer: bool = False,
        is_typescript: bool = False,
    ):
        if is_typescript:
            script_text = self.__process_transpiled_script_for_js_to_py(script_text)
            response = requests.post(
                "http://localhost:5001/transpile",
                data=script_text,
                headers={"Content-Type": "application/json"},
            )
            if response.status_code != 200:
                raise Exception(
                    "Transpilation of TypeScript failed with status code: "
                    + str(response.status_code)
                )
            script_text = response.text.strip().replace("\\n", "\n").replace('\\"', '"')
            print(script_text)

            # TO-DO
            # Load another general imports - variables
            # concatenate them with script
            # Load the call:
            call = """
                var canvas2 = document.createElement("CANVAS"); //RENAMED
                var context2 = canvas2.getContext("2d");
                var circleRadius = 5;
                var  numberIterations = 5;
                var  lineLength = 500;
                var  thickness = 2;
                var  squareSideLength = lineLength / Math.pow(2, numberIterations);
                drawAnkletModMain(context2, circleRadius, numberIterations, thickness);
                function getData() { return initialGraphRoot; }
                getData();
                """
        if use_mini_racer:
            return self.mini_racer.eval(script_text)
        try:
            return js2py.eval_js(script_text)
        except JsException as e:
            print(e)
            return js2py.eval_js6(script_text)

    def __remove_context_content(self, script_text: str) -> str:
        # script_text = re.sub(r"[^\n(]*context\.[^\n=]+\n", "", script_text)
        script_text = re.sub(r"context\.[^\n]+\n", "", script_text)
        script_text = re.sub(r"\n[^\n]+document\.[^\n]+\n", "", script_text)
        script_text = re.sub(
            r"\s+(const|var|let)\s+([^\s]+)\s*=\s*\w+\.getContext\(\s*['\"]2d[\"']\s*\)[^\n;]*[\n;]",
            r"\1 \2 = {};",
            script_text,
        )
        return script_text

    def __unwrap(self, script_text: str) -> str:
        return (
            script_text[script_text.find("`") + 1 :].strip()[:-1].replace("export ", "")
        )

    def load_fractal(
        self,
        fractal_script_path: str,
        is_wrapped: bool = False,
        is_typescript: bool = False,
    ) -> str:
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
        self,
        fractal_script_path: str,
        variable_name: str,
        is_wrapped: bool = False,
        is_typescript: bool = False,
    ) -> str:
        with open(fractal_script_path, "r", encoding="latin-1") as file:
            fractal_content = file.read()
        fractal_content = (
            self.__unwrap(fractal_content) if is_wrapped else fractal_content
        )
        # fractal_content = self.__remove_context_content(fractal_content)
        branch_data_script = (
            fractal_content
            + "; function getData() { return JSON.stringify("
            + variable_name
            + " ); } getData();"
        )
        return self.__get_branches(branch_data_script, is_typescript=is_typescript)

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
