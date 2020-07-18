# Lin

An attempt to create a parser and interpreter for a language which resembles a lot Kotlin but borrow elements from JavaScript.

Lin most likely will never fully parse Kotlin, but might ending up parsing _a lot_ of Kotlin.

## Implemented (Parser)

- Booleans, Null
- Ints, Longs, Floats, Doubles, Chars
    - **CAVEATS**:
        - Parsing needs to be improved but works
        - Characters are somewhat sketchy
- Strings
    - **CAVEATS**:
        - Parsing needs to be improved but works
        - Raw strings and template strings not yet implemented
- Variables
    - Vals, Vars, Destructuring variables
- access/assignments of variables
- Function declaration (named, anonymous)
- Function invocation
- Infix function invocation
- Property access/assignment
- Subscript access/assignment (`a[...]`, `a[...] = b`)
- While/Do-While loops
- For Loops
- If/else (both as statement and expression)
- Return/Throw
- Null-safe operator (`?.`)
- Not-null assertion operator (`!!`)
- Unary operations: `!a`, `+a`, `-a`
- Binary operations:
    - `a+b`, `a-b`, `a*b`, `a/b`, `a%b`
    - `a==b`, `a!=b`, `a&&b`, `a||b`, `a<b`, `a<=b`, `a>b`, `a>=b`
    - `a ?: b`, `a in b`, `a !in b`, `a..b`
- Increment (`++a`, `a++`) and Decrement (`--a`, `a--`) operations

## Missing

- The type system, and by extent:
    - The `is`, `!is`, `as`, `as?`, `typeof` operators
    - Types on variables, parameters, return types
    - SAM constructors, if get implemented at all
    - Type aliases
    - Generics
- Declaration of extension functions (Requires a minimal type system)
- Declaration of infix functions
- Declaration of operator functions
- Declaration of function-based properties
- Break/Continue on Loops
- Labels on Loops
- When
- Lambdas
- Property delegation
- Visibility Modifiers (public, private, internal)
- Classes, Objects and Interfaces, and by extent:
    - Interface delegation
    - Functional interfaces
    - Enum classes
    - Data classes
- Binary-assignment (`a+=b`, `a-=b`, `a*=b`, `a/=b`, `a%=b`)
- The interpreter

## Stuff that will most likely be different

- Mostly everything regarding to imports/packages will be different.
    - **Lin** will most likely follow Python/Javascript ideas in this scenario.
    - It is almost sure that the `package x; import ...` won't happen.
- All **Lin** files will be script files.
    - This means that loading classes means loading and executing the script to get the classes.
- Inline and _tailrec_ functions will most likely never get implemented.
- Standard library and everything related (Coroutines, Reflections)
- Exceptions
- Annotations